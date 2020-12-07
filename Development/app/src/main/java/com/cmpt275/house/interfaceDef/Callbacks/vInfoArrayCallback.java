package com.cmpt275.house.interfaceDef.Callbacks;

import com.cmpt275.house.classDef.infoClass.votingInfo;

public interface vInfoArrayCallback {
    void onReturn(votingInfo[] vInfos, boolean success, String errorMessage);
}
