package com.cmpt275.house.interfaceDef.Callbacks;

import com.cmpt275.house.classDef.infoClass.votingInfo;

public interface vInfoCallback {
    void onReturn(votingInfo vInfo, boolean success, String errorMessage);
}
