package com.spring.rest.entities;

public enum MemberStatus {

    //teacher researcher types
    PR,
    MCF,
    MCF_HDR,
    ECC,
    LRU,
    PHD_Student,
    PostPHD,
    ITRF, //Includes IGE, IGE, ADM...
    Prag,
    Associate,
    //Some additional ones that can be useful when none of the above applies
    Intern,
    Contractless;
}
