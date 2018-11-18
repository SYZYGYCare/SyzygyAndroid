package com.dollop.syzygy.data;

import com.dollop.syzygy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by awidiyadew on 15/09/16.
 */
public class ClientCustomDataProvider {

    private static final int MAX_LEVELS = 3;

    private static final int LEVEL_1 = 1;
    private static final int LEVEL_2 = 2;
    private static final int LEVEL_3 = 3;

    private static List<BaseItem> mMenu = new ArrayList<>();

    public static List<BaseItem> getInitialItems() {
        //return getSubItems(new GroupItem("root"));

        List<BaseItem> rootMenu = new ArrayList<>();

        /*
        * ITEM = TANPA CHILD
        * GROUPITEM = DENGAN CHILD
        * */

        rootMenu.add(new Item(R.drawable.ic_hire_your_caregiver_drawer_non_active, "Hire Your Caregiver"));
        rootMenu.add(new Item(R.drawable.ic_your_hire_drawer_non_active, "Your Hires"));
        rootMenu.add(new Item(R.drawable.ic_refer_earn_drawer_non_active, "Refer & Earn"));
        rootMenu.add(new Item(R.drawable.ic_emergency_drawer_non_active_client, "Emergency Contact"));
        rootMenu.add(new Item(R.drawable.ic_refer_earn_drawer_non_active, "Add Someone"));
        rootMenu.add(new Item(R.drawable.ic_support_drawer_non_active, "Support"));
        rootMenu.add(new Item(R.drawable.ic_about_drawer_non_active_client, "About"));
        rootMenu.add(new Item(R.drawable.ic_about_drawer_non_active_client, "Privacy Policy"));
        rootMenu.add(new Item(R.drawable.ic_about_drawer_non_active_client, "Terms & Conditions"));
        rootMenu.add(new GroupItem(R.drawable.ic_setting_drawer_non_active_client, "Setting"));
        rootMenu.add(new Item(R.drawable.ic_logout_drawer_non_active_client, "Logout"));

        return rootMenu;
    }

    public static List<BaseItem> getSubItems(BaseItem baseItem) {

        List<BaseItem> result = new ArrayList<>();
        int level = ((GroupItem) baseItem).getLevel() + 1;
        String menuItem = baseItem.getName();

        if (!(baseItem instanceof GroupItem)) {
            throw new IllegalArgumentException("GroupItem required");
        }

        GroupItem groupItem = (GroupItem) baseItem;
        if (groupItem.getLevel() >= MAX_LEVELS) {
            return null;
        }

        /*
        * HANYA UNTUK GROUP-ITEM
        * */
        switch (level) {
            case LEVEL_1:
                switch (menuItem.toString()) {
                    case "Hire Your Caregiver":
                        // result = getListSetting();
                        break;
                    case "Your Hires":
                        // result = getListLogout();
                        break;
                    case "Emergency Contact":
                        // result = getListEmergencyContact();
                        break;
                    case "Refer & Earn":
                        //result = getListHomet();
                        break;
                    case "Setting":
                        result = getListSetting();
                        break;
                    case "Logout":
                        //result = getListHomet();
                        break;
                    case "Add Senior":
                        //result = getListHomet();
                        break;
                    case "About":
                        //result = getListHomet();
                        break;

                }
                break;

            case LEVEL_2:
                switch (menuItem) {
//                    case "GROUP 1" :
//                        result = getListGroup1();
//                        break;
//                    case "GROUP X" :
//                        result = getListGroupX();
//                        break;
                }
                break;
        }

        return result;
    }

    public static boolean isExpandable(BaseItem baseItem) {
        return baseItem instanceof GroupItem;
    }

    private static List<BaseItem> getListKategori() {

        List<BaseItem> list = new ArrayList<>();
        list.add(new Item(R.drawable.ic_ambulance_home, "Ambulance"));
        list.add(new Item(R.drawable.ic_caregiver_home, "Care Giver"));
        return list;
    }

    private static List<BaseItem> getListSetting() {

        List<BaseItem> list = new ArrayList<>();
        list.add(new Item(R.drawable.ic_password_client, "Change Password"));
        return list;
    }

    private static List<BaseItem> getListLogout() {

        List<BaseItem> list = new ArrayList<>();
        list.add(new Item(R.drawable.ic_change_password, "Logout"));
        return list;
    }

    private static List<BaseItem> getListEmergencyContact() {

        List<BaseItem> list = new ArrayList<>();
        list.add(new Item(R.drawable.ic_change_password, "Emergency Contact"));


        return list;
    }

    private static List<BaseItem> getListHomet() {

        List<BaseItem> list = new ArrayList<>();
        list.add(new Item(R.drawable.ic_change_password, "Home"));


        return list;
    }

//    private static List<BaseItem> getListKategoriLainnya(){
//
//        List<BaseItem> list = new ArrayList<>();
//        GroupItem groupItem = new GroupItem("GROUP X");
//        groupItem.setLevel(groupItem.getLevel() + 1);
//
//        list.add(new Item("ITEM A"));
//        list.add(new Item("ITEM B"));
//        list.add(groupItem);
//
//        return list;
//    }
//
//    private static List<BaseItem> getListGroup1(){
//        List<BaseItem> list = new ArrayList<>();
//        list.add(new Item("CHILD OF G1-A"));
//        list.add(new Item("CHILD OF G1-B"));
//
//        return list;
//    }
//
//    private static List<BaseItem> getListGroupX(){
//        List<BaseItem> list = new ArrayList<>();
//        list.add(new Item("CHILD OF GX-A"));
//        list.add(new Item("CHILD OF GX-B"));
//
//        return list;
//    }

}
