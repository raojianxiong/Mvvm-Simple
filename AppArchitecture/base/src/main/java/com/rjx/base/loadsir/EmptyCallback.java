package com.rjx.base.loadsir;

import com.kingja.loadsir.callback.Callback;
import com.rjx.base.R;

/**
 * @author Jianxiong Rao
 */
public class EmptyCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.layout_empty;
    }
}
