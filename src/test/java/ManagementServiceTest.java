import com.cj.TimeStamp;
import org.junit.jupiter.api.Test;
import com.cj.ManagementService;

public class ManagementServiceTest {
    @Test
    public void testFetchingById() {
        ManagementService ms = new ManagementService();
        int lastUser = ms.getLastUserID();
        ms.addUser(Integer.toString(lastUser+1), "Nils","Holgerson", 1, 12);
        String username = ms.getUsername(Integer.toString(lastUser+1));
        System.out.println(username + " = Nils Holgerson");
        System.out.println(username.equals("Nils Holgerson"));
    }

    @Test
    public void addExistingUserById() {
        ManagementService ms = new ManagementService();
        int lastUser = ms.getLastUserID();
        String response = ms.addUser(Integer.toString(lastUser), "Nils","Holgerson", 1, 12);

        System.out.println(response + " = A user with this ID already exists!");
        System.out.println("A user with this ID already exists!".equals(response));
    }


    @Test
    public void addDemandForUnknownUserId() {
        ManagementService ms = new ManagementService();
        int lastUser = ms.getLastUserID();
        String response = ms.addDemand("100", lastUser+1, 1,2,
                new TimeStamp("2018-08-07T09:01:12", true), new TimeStamp("2018-08-07T12:01:12", true),
                "Golf", "100 PS", "comfort", "basic");

        System.out.println(response + " = No user found under this ID!");
        System.out.println("No user found under this ID!".equals(response));
    }


    @Test
    public void deleteUserWithOpenDemand() {
        ManagementService ms = new ManagementService();
        int lastUser = ms.getLastUserID();
        int lastDemandNum = ms.getLastDemandNum();
        ms.addDemand(Integer.toString(lastDemandNum+1), lastUser, 1,2,
                new TimeStamp("2018-08-07T09:01:12", true), new TimeStamp("2018-08-07T12:01:12", true),
                "Golf", "100 PS", "comfort", "basic");
        // the relationship logic of the database would actually throw an exception if the query went through! cool! :)
        String response = ms.deleteUser(Integer.toString(lastUser));

        System.out.println(response + " = A user with this ID doesn't exist or has open demands!");
        System.out.println("A user with this ID doesn't exist or has open demands!".equals(response));
    }



}
