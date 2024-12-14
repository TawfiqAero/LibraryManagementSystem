class Member {
    private int memberId;
    private String name;

    public Member(int memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String toFileFormat() {
        return memberId + "," + name;
    }

    @Override
    public String toString() {
        return memberId + ": " + name;
    }
}

