package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.interfaceDef.Callbacks.updateCallback;

public interface home {
    void logout(updateCallback callback);
    void updateUserInfo(String user_id, updateCallback callback);
}
