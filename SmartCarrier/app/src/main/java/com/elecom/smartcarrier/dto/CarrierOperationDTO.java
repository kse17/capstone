package com.elecom.smartcarrier.dto;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class CarrierOperationDTO {
    // Service state
    private String addOperation;
    private String deleteOperation;
    private String findOperation;

    public CarrierOperationDTO(){
        super();
    }

    public CarrierOperationDTO(String addOperation, String deleteOperation, String findOperation) {
        super();

        this.addOperation = addOperation;
        this.deleteOperation = deleteOperation;
        this.findOperation = findOperation;
    }


    public String getAddOperation() {
        return this.addOperation;
    }
    public void setAddOperation(String addOperation) {
        this.addOperation = addOperation;
    }

    public String getDeleteOperation() {
        return this.deleteOperation;
    }
    public void setDeleteOperation(String deleteOperation) {
        this.deleteOperation = deleteOperation;
    }

    public String getFindOperation() {
        return this.findOperation;
    }
    public void setFindOperation(String findOperation) {
        this.findOperation = findOperation;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> carriersOperations = new HashMap<>();

        carriersOperations.put("addOperation", addOperation);
        carriersOperations.put("deleteOperation", deleteOperation);
        carriersOperations.put("findOperation", findOperation);

        return carriersOperations;
    }

    @Override
    public String toString() {
        return "CarrierOperation [addOperation = " + addOperation + ", deleteOperation = " + deleteOperation
                + ", findOperation = " + findOperation + "]";
    }

}