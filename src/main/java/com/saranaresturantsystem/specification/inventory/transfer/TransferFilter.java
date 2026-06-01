package com.saranaresturantsystem.specification.inventory.transfer;

import lombok.Data;

import java.util.Date;

@Data
public class TransferFilter {
    private  String transferNo ;
    private  Long fromStoreId ;
    private  Long toStoreId ;
    private Date fromDate ;
}
