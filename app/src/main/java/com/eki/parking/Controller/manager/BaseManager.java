package com.eki.parking.Controller.manager;

import android.content.Context;

import com.eki.parking.Controller.impl.IAppContext;
import com.hill.devlibs.manager.LibBaseManager;


/**
 * Created by Hill on 2017/10/11.
 */

public abstract class BaseManager extends LibBaseManager
                                  implements IAppContext {


    public BaseManager(Context context){
        super(context);
    }


}
