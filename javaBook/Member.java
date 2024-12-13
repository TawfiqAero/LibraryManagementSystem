import java.io.*;

public class Member {
    private int memberId;
    private String name;

    public Member(int memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }

    public static Member getMemberById(int memberId) {
        File membersFile = new File("members.txt");
        if (!membersFile.exists()) {
            System.out.println("Error: members.txt file not found.");
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(membersFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] memberData = line.split(",");
                if (Integer.parseInt(memberData[0]) == memberId) {
                    return new Member(memberId, memberData[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading members.txt: " + e.getMessage());
        }
        return null;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }
}