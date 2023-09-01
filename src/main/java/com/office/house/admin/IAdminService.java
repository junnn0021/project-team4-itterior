package com.office.house.admin;

import java.util.Map;

public interface IAdminService {


    AdminDto adminLoginConfirm(AdminDto adminDto);

    int createAccountConfirm(AdminDto adminDto);

    int adminModifyConfirm(AdminDto adminDto);

    int adminDeleteConfirm(AdminDto adminDto);

    Map<String, Object> adminList(String keyWord, int pageNum, int amount);

    AdminDto adminListDetail(int a_no);

    int adminListModifyConfirm(AdminDto adminDto);
}
