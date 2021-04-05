package components.salesforce.common;

import components.BasePageComponent;

public class AccountManagementComponent extends BasePageComponent {

    public static String COMPONENT_NAME = "AccountManagementComponent";
    private static AccountManagementComponent instance = null;

    public static AccountManagementComponent  getInstance() {
        if (instance == null)
            instance = new AccountManagementComponent();

        return instance;
    }

    public void getAccountMembers(){
        getAttribute(LOCATORS.getBy(COMPONENT_NAME,"ACCOUNT_MEMBERS"),"href");

    }


    public void  deactivateMember(String userName){

    }

    public void  activateMember(String userName){

    }

    public void editMember(String userName){

    }//more

    public String filterByFullName(String filter){
        String member=null;


        return member;
    }
    public String filterByEmail(String email){
        String member=null;
        return member;
    }
    public String filterByTitle(String title){
        String member=null;
        return member;
    }
    public boolean filterByActive(boolean active,String[] result){
        return true;
    }

    public boolean isPageExist(){
        return true;
    }

    public void addMember(/*list of fields*/){


    }


}
