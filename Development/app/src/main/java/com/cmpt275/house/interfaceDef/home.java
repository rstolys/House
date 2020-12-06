package com.cmpt275.house.interfaceDef;

import com.cmpt275.house.interfaceDef.Callbacks.booleanCallback;

public interface home {
    void logout(updateUI callback);
    void updateUserInfo(String user_id, booleanCallback callback);
}
