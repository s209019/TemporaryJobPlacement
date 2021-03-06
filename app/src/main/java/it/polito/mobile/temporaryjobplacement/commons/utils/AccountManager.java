package it.polito.mobile.temporaryjobplacement.commons.utils;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRole;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.Map;

import it.polito.mobile.temporaryjobplacement.model.Company;
import it.polito.mobile.temporaryjobplacement.model.Student;

/**
 *
 */
public class AccountManager {




    public static ParseUser login(String username, String password) throws ParseException {
        return ParseUser.logIn(username, password);
    }


    public static void signup(String username, String password,String userType) throws ParseException {

        Map<String,String> roleName_roleId=new HashMap<String, String>();
        roleName_roleId.put("company","vbuJKCEqnh");
        roleName_roleId.put("student","ghdgTvp24l");

        //create ParseUser
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        //user.setEmail("email@example.com");
        // other fields can be set just like with ParseObject
        user.put("userType", userType);
        user.signUp();

        //adding relative role
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_Role");
        String groupRole=roleName_roleId.get(userType);
        ParseRole role= (ParseRole) query.get(groupRole);
        role.getUsers().add(user);
        role.save();

        if(userType.equals("student")) {
            Student student = new Student();
            student.put("user", user);
            student.setFirstName("");
            student.setLastName("");
            student.save();

            //TODO: Settare l'ACL
        } else if(userType.equals("company")) {
            Company company = new Company();
            company.put("user", user);
            company.setName("");
            company.save();

            //TODO: Settare l'ACL
        }
        ParseUser.logOut();

    }




    public static boolean checkIfLoggedIn(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            return true;
        } else {
            // show the signup or login screen
            return false;
        }
    }



    public static ParseUser getCurrentUser() throws Exception {
       if(checkIfLoggedIn())return ParseUser.getCurrentUser();
        else throw new Exception("No user logged");

    }

    public static Student getCurrentStudentProfile() throws Exception {
        if(checkIfLoggedIn() && getCurrentUserType().equals("student"))
            return Student.getQuery().include("coverLetters").include("cv0").include("cv1").include("cv2").include("cv3").include("cv4").whereEqualTo("user", AccountManager.getCurrentUser()).getFirst();
        else
            throw new Exception("No student logged");

    }

    public static Company getCurrentCompanyProfile() throws Exception {
        if(checkIfLoggedIn() && getCurrentUserType().equals("company"))
            return Company.getQuery().include("defaultMessages").whereEqualTo("user", AccountManager.getCurrentUser()).getFirst();
        else
            throw new Exception("No company logged");

    }



    public static String getCurrentUserType() throws Exception {
        return getCurrentUser().get("userType").toString();
    }



    public static void setPrivateUserWriteAccess(ParseObject object){
        ParseACL parseACL=new ParseACL(ParseUser.getCurrentUser());
        parseACL.setPublicReadAccess(true);
        object.setACL(parseACL);

    }




   public static void logout(){
       ParseUser.logOut();

   }

}
