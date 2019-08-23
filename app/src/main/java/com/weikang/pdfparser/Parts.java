package com.weikang.pdfparser;

class Parts {
    private String name;

    public Parts(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return name.substring(0, 16);
    }

    public boolean isEqual(Parts o) {
        return o.getName() == this.getName();
    }
}
