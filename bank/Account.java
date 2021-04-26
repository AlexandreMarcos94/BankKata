package bank;

class Account {

    // Attributes
    private String name;
    private Integer balance;
    private Integer threshold;
    private Boolean isblock;

    // Constructor
    public Account(String name, Integer balance, Integer threshold, Boolean isblock){
        this.name = name;
        this.balance = balance;
        this.threshold = threshold;
        this.isblock = isblock;
    }

    // Methods
    // TODO
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public Boolean getIsblock() {
        return isblock;
    }

    public void setIsblock(Boolean isblock) {
        this.isblock = isblock;
    }

    public String toString() {
        // TODO
        return "";
    }
}
