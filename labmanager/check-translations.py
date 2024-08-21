#!/usr/bin/env python3

import sys
import os
import re
import argparse

def read_property_file(filename, sep='=', comment_char='#'):
	props = {}
	with open(filename, "rt", encoding='iso-8859-1') as f:
		for line in f:
			l = line.strip()
			if l and not l.startswith(comment_char):
				key_value = l.split(sep)
				if len(key_value) > 1:
					key = key_value[0].strip()
					value = sep.join(key_value[1:]).strip().strip('"') 
					props[key] = value 
	return props

def find_translation_files(filename):
	other_files = []
	translation_folder = os.path.dirname(filename)
	basename = os.path.basename(filename)
	pattern = re.compile("^(.+)((?:\.[^.]+)+)$")
	matcher = pattern.match(basename)
	if matcher:
		prefix = matcher.group(1)
		postfix = matcher.groups(2)
		for root, directories, files in os.walk(translation_folder):
			for file in files:
				if file.startswith(prefix) and file.endswith(postfix) and file != basename:
					other_files.append(os.path.join(root, file))
		return other_files
	else:
		raise("Invalid basename format: " + basename)

def validate_translation_files(filename, reference_properties):
	translation_files = find_translation_files(filename)
	has_error = False
	for translation_file in translation_files:
		other_properties = set(read_property_file(translation_file).keys())
		missed_properties = reference_properties.difference(other_properties)
		if missed_properties:
			print("\nERROR: missed properties in " + translation_file + "\n\n" + ("\n".join(sorted(missed_properties))))
			has_error = True
		added_properties = other_properties.difference(reference_properties)
		if added_properties:
			print("\nERROR: unnecessary properties in " + translation_file + "\n\n" + ("\n".join(sorted(added_properties))))
			has_error = True
	if has_error:
		sys.exit(255)

def validate_java_files(java_folder, used_prefixes, reference_properties, localized_properties):
	validate_java_files_one(java_folder, used_prefixes, reference_properties, None)
	for local_file, props in localized_properties.items():
		validate_java_files_one(java_folder, used_prefixes, props, local_file)

def validate_java_files_one(java_folder, used_prefixes, reference_properties, file_label):
	remaining = set(reference_properties)
	if used_prefixes:
		for prop in set(reference_properties):
			for prefix in used_prefixes:
				if prop.startswith(prefix + '.'):
					remaining.remove(prop)
	for root, directories, files in os.walk(java_folder):
		for filename in files:
			if filename.endswith(".java"):
				full_path = os.path.join(root, filename)
				with open(full_path, "rt") as f:
					file_content = str(f.read())
				for prop in set(remaining):
					if ("\"" + prop + "\"") in file_content:
						remaining.remove(prop)
	if remaining:
		# Several properties have been used after a Java string concatenation (for enum)
		if file_label:
			print("\nERROR: unused properties in Java code from: " + file_label + "\n\n" + ("\n".join(sorted(remaining))))
		else:
			print("\nERROR: unused properties in Java code\n\n" + ("\n".join(sorted(remaining))))
		sys.exit(255)

def read_localized_properties(filename):
	translation_files = find_translation_files(filename)
	localized_properties = dict()
	for translation_file in translation_files:
		other_properties = set(read_property_file(translation_file).keys())
		localized_properties[str(translation_file)] = other_properties
	return localized_properties

def main(args):
	reference_translation_file = args.translation_file
	reference_properties = set(read_property_file(reference_translation_file).keys())
	localized_properties = read_localized_properties(reference_translation_file)
	if args.java:
		validate_java_files(args.jfolder, args.use, reference_properties, localized_properties)
	if args.trans:
		validate_translation_files(args.translation_file, reference_properties)

if __name__ == "__main__":
	parser = argparse.ArgumentParser(description="Validation of the translation constants' definitions")
	defaultJFolder = os.path.join(".", "src", "main", "java")
	parser.add_argument("--trans", help="Validate the translation files", action=argparse.BooleanOptionalAction, default=True)
	parser.add_argument("--java", help="Validate the Java files", action=argparse.BooleanOptionalAction, default=True)
	parser.add_argument("--jfolder", help="Path to the folder that contains the Java code to analyze; default is: " + defaultJFolder, default=defaultJFolder, metavar='PATH')
	parser.add_argument("--use", help="Specify the prefix in the property name for properties that are always assumed to be used in Java code, e.g. in some enumeration code", metavar='PREFIX', action='append')
	parser.add_argument("translation_file", help="Path to the main translation file (without \"_country\" markers)")
	args = parser.parse_args()
	main(args)
