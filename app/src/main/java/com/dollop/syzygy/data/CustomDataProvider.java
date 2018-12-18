package com.dollop.syzygy.data;

import com.dollop.syzygy.R;
import com.dollop.syzygy.sohel.SavedData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by awidiyadew on 15/09/16.
 */
public class CustomDataProvider {

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
        rootMenu.add(new Item(R.drawable.ic_house_building_outline, "Home"));
        rootMenu.add(new GroupItem(R.drawable.ic_register_for_non_active, "Register For"));
        rootMenu.add(new Item(R.drawable.ic_payment_drawer_non_active, "Your Hires"));
//        rootMenu.add(new Item(R.drawable.ic_emergency_drawer_non_active, "Emergency Contact"));
        rootMenu.add(new GroupItem(R.drawable.ic_setting_drawer_non_active, "Setting"));
        rootMenu.add(new Item(R.drawable.ic_payment_drawer_non_active, "Account Detail"));
        rootMenu.add(new Item(R.drawable.ic_trust_badges_non_active, "Trust Badges"));
        rootMenu.add(new Item(R.drawable.ic_about_drawer_non_active, "About"));
        rootMenu.add(new Item(R.drawable.ic_privacy_policy_non_active, "Privacy Policy"));
        rootMenu.add(new Item(R.drawable.ic_terms_service_non_active, "Terms & Conditions"));
        rootMenu.add(new Item(R.drawable.ic_logout_drawer_non_active, "Logout"));

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
                    case "Register For":
                        result = getListKategori();
                        break;
                    case "Setting":
                        result = getListSetting();
                        break;
                    case "Logout":
                        result = getListLogout();
                        break;
                    case "Emergency Contact":
                        result = getListEmergencyContact();
                        break;
                    case "Home":
                        result = getListHomet();

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

        if (!SavedData.getSaveType().equals("")) {
            if (SavedData.getSaveType().equals("1")) {
                list.add(new Item(R.drawable.doctor, "Care Giver"));
            } else if (SavedData.getSaveType().equals("2")) {
                list.add(new Item(R.drawable.ambulance, "Ambulance"));
            } else {
                list.add(new Item(R.drawable.ambulance, "Ambulance"));
                list.add(new Item(R.drawable.doctor, "Care Giver"));
            }
        } else {
            list.add(new Item(R.drawable.ambulance, "Ambulance"));
            list.add(new Item(R.drawable.doctor, "Care Giver"));
        }

        return list;
    }

    private static List<BaseItem> getListSetting() {

        List<BaseItem> list = new ArrayList<>();
        list.add(new Item(R.drawable.ic_lock, "Change Password"));
        return list;
    }

    private static List<BaseItem> getListLogout() {

        List<BaseItem> list = new ArrayList<>();
        list.add(new Item(R.drawable.ic_lock, "Logout"));


        return list;
    }

    private static List<BaseItem> getListEmergencyContact() {

        List<BaseItem> list = new ArrayList<>();
        list.add(new Item(R.drawable.ic_lock, "Emergency Contact"));


        return list;
    }

    private static List<BaseItem> getListHomet() {

        List<BaseItem> list = new ArrayList<>();
        list.add(new Item(R.drawable.ic_lock, "Home"));


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
