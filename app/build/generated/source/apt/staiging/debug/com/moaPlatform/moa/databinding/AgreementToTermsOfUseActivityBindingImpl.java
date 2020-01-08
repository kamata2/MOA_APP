package com.moaPlatform.moa.databinding;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AgreementToTermsOfUseActivityBindingImpl extends AgreementToTermsOfUseActivityBinding implements com.moaPlatform.moa.generated.callback.OnCheckedChangeListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.scrollView, 4);
        sViewsWithIds.put(R.id.headForm, 5);
        sViewsWithIds.put(R.id.txtAgreeStart, 6);
        sViewsWithIds.put(R.id.border, 7);
        sViewsWithIds.put(R.id.termsAndCondition, 8);
        sViewsWithIds.put(R.id.demoLine, 9);
        sViewsWithIds.put(R.id.gpsAllRead, 10);
        sViewsWithIds.put(R.id.done, 11);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    @Nullable
    private final android.widget.CompoundButton.OnCheckedChangeListener mCallback2;
    @Nullable
    private final android.widget.CompoundButton.OnCheckedChangeListener mCallback3;
    @Nullable
    private final android.widget.CompoundButton.OnCheckedChangeListener mCallback1;
    // values
    // listeners
    // Inverse Binding Event Handlers
    private androidx.databinding.InverseBindingListener allCheckBoxandroidCheckedAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of CustomSafeBox.unBox(agreementToTermsofUseViewModel.allAgree.getValue())
            //         is agreementToTermsofUseViewModel.allAgree.setValue((java.lang.Boolean) CustomSafeBox.boxBoolean(callbackArg_0))
            boolean callbackArg_0 = allCheckBox.isChecked();
            // localize variables for thread safety
            // agreementToTermsofUseViewModel.allAgree != null
            boolean agreementToTermsofUseViewModelAllAgreeJavaLangObjectNull = false;
            // agreementToTermsofUseViewModel
            com.moaPlatform.moa.agreement_to_terms_of_use.model.view_model.AgreementToTermsOfUseViewModel agreementToTermsofUseViewModel = mAgreementToTermsofUseViewModel;
            // CustomSafeBox.unBox(agreementToTermsofUseViewModel.allAgree.getValue())
            boolean customSafeBoxUnBoxAgreementToTermsofUseViewModelAllAgree = false;
            // agreementToTermsofUseViewModel != null
            boolean agreementToTermsofUseViewModelJavaLangObjectNull = false;
            // agreementToTermsofUseViewModel.allAgree.getValue()
            java.lang.Boolean agreementToTermsofUseViewModelAllAgreeGetValue = null;
            // agreementToTermsofUseViewModel.allAgree
            androidx.lifecycle.MutableLiveData<java.lang.Boolean> agreementToTermsofUseViewModelAllAgree = null;



            agreementToTermsofUseViewModelJavaLangObjectNull = (agreementToTermsofUseViewModel) != (null);
            if (agreementToTermsofUseViewModelJavaLangObjectNull) {


                agreementToTermsofUseViewModelAllAgree = agreementToTermsofUseViewModel.allAgree;

                agreementToTermsofUseViewModelAllAgreeJavaLangObjectNull = (agreementToTermsofUseViewModelAllAgree) != (null);
                if (agreementToTermsofUseViewModelAllAgreeJavaLangObjectNull) {



                    com.moaPlatform.moa.util.data_binding.CustomSafeBox.boxBoolean(callbackArg_0);


                    agreementToTermsofUseViewModelAllAgree.setValue(((java.lang.Boolean) (com.moaPlatform.moa.util.data_binding.CustomSafeBox.boxBoolean(callbackArg_0))));
                }
            }
        }
    };
    private androidx.databinding.InverseBindingListener checkBoxEventAgreeandroidCheckedAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of CustomSafeBox.unBox(agreementToTermsofUseViewModel.eventAgree.getValue())
            //         is agreementToTermsofUseViewModel.eventAgree.setValue((java.lang.Boolean) CustomSafeBox.boxBoolean(callbackArg_0))
            boolean callbackArg_0 = checkBoxEventAgree.isChecked();
            // localize variables for thread safety
            // agreementToTermsofUseViewModel.eventAgree
            androidx.lifecycle.MutableLiveData<java.lang.Boolean> agreementToTermsofUseViewModelEventAgree = null;
            // CustomSafeBox.unBox(agreementToTermsofUseViewModel.eventAgree.getValue())
            boolean customSafeBoxUnBoxAgreementToTermsofUseViewModelEventAgree = false;
            // agreementToTermsofUseViewModel.eventAgree != null
            boolean agreementToTermsofUseViewModelEventAgreeJavaLangObjectNull = false;
            // agreementToTermsofUseViewModel
            com.moaPlatform.moa.agreement_to_terms_of_use.model.view_model.AgreementToTermsOfUseViewModel agreementToTermsofUseViewModel = mAgreementToTermsofUseViewModel;
            // agreementToTermsofUseViewModel.eventAgree.getValue()
            java.lang.Boolean agreementToTermsofUseViewModelEventAgreeGetValue = null;
            // agreementToTermsofUseViewModel != null
            boolean agreementToTermsofUseViewModelJavaLangObjectNull = false;



            agreementToTermsofUseViewModelJavaLangObjectNull = (agreementToTermsofUseViewModel) != (null);
            if (agreementToTermsofUseViewModelJavaLangObjectNull) {


                agreementToTermsofUseViewModelEventAgree = agreementToTermsofUseViewModel.eventAgree;

                agreementToTermsofUseViewModelEventAgreeJavaLangObjectNull = (agreementToTermsofUseViewModelEventAgree) != (null);
                if (agreementToTermsofUseViewModelEventAgreeJavaLangObjectNull) {



                    com.moaPlatform.moa.util.data_binding.CustomSafeBox.boxBoolean(callbackArg_0);


                    agreementToTermsofUseViewModelEventAgree.setValue(((java.lang.Boolean) (com.moaPlatform.moa.util.data_binding.CustomSafeBox.boxBoolean(callbackArg_0))));
                }
            }
        }
    };
    private androidx.databinding.InverseBindingListener checkBoxGpsAgreeandroidCheckedAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of CustomSafeBox.unBox(agreementToTermsofUseViewModel.gpsAgree.getValue())
            //         is agreementToTermsofUseViewModel.gpsAgree.setValue((java.lang.Boolean) CustomSafeBox.boxBoolean(callbackArg_0))
            boolean callbackArg_0 = checkBoxGpsAgree.isChecked();
            // localize variables for thread safety
            // agreementToTermsofUseViewModel
            com.moaPlatform.moa.agreement_to_terms_of_use.model.view_model.AgreementToTermsOfUseViewModel agreementToTermsofUseViewModel = mAgreementToTermsofUseViewModel;
            // agreementToTermsofUseViewModel.gpsAgree
            androidx.lifecycle.MutableLiveData<java.lang.Boolean> agreementToTermsofUseViewModelGpsAgree = null;
            // agreementToTermsofUseViewModel.gpsAgree != null
            boolean agreementToTermsofUseViewModelGpsAgreeJavaLangObjectNull = false;
            // agreementToTermsofUseViewModel != null
            boolean agreementToTermsofUseViewModelJavaLangObjectNull = false;
            // CustomSafeBox.unBox(agreementToTermsofUseViewModel.gpsAgree.getValue())
            boolean customSafeBoxUnBoxAgreementToTermsofUseViewModelGpsAgree = false;
            // agreementToTermsofUseViewModel.gpsAgree.getValue()
            java.lang.Boolean agreementToTermsofUseViewModelGpsAgreeGetValue = null;



            agreementToTermsofUseViewModelJavaLangObjectNull = (agreementToTermsofUseViewModel) != (null);
            if (agreementToTermsofUseViewModelJavaLangObjectNull) {


                agreementToTermsofUseViewModelGpsAgree = agreementToTermsofUseViewModel.gpsAgree;

                agreementToTermsofUseViewModelGpsAgreeJavaLangObjectNull = (agreementToTermsofUseViewModelGpsAgree) != (null);
                if (agreementToTermsofUseViewModelGpsAgreeJavaLangObjectNull) {



                    com.moaPlatform.moa.util.data_binding.CustomSafeBox.boxBoolean(callbackArg_0);


                    agreementToTermsofUseViewModelGpsAgree.setValue(((java.lang.Boolean) (com.moaPlatform.moa.util.data_binding.CustomSafeBox.boxBoolean(callbackArg_0))));
                }
            }
        }
    };

    public AgreementToTermsOfUseActivityBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 12, sIncludes, sViewsWithIds));
    }
    private AgreementToTermsOfUseActivityBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 3
            , (android.widget.CheckBox) bindings[1]
            , (android.view.View) bindings[7]
            , (android.widget.CheckBox) bindings[3]
            , (android.widget.CheckBox) bindings[2]
            , (android.widget.ImageView) bindings[9]
            , (android.widget.Button) bindings[11]
            , (android.widget.TextView) bindings[10]
            , (android.widget.LinearLayout) bindings[5]
            , (android.widget.ScrollView) bindings[4]
            , (android.widget.TextView) bindings[8]
            , (android.widget.TextView) bindings[6]
            );
        this.allCheckBox.setTag(null);
        this.checkBoxEventAgree.setTag(null);
        this.checkBoxGpsAgree.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        mCallback2 = new com.moaPlatform.moa.generated.callback.OnCheckedChangeListener(this, 2);
        mCallback3 = new com.moaPlatform.moa.generated.callback.OnCheckedChangeListener(this, 3);
        mCallback1 = new com.moaPlatform.moa.generated.callback.OnCheckedChangeListener(this, 1);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x10L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.agreementToTermsofUseViewModel == variableId) {
            setAgreementToTermsofUseViewModel((com.moaPlatform.moa.agreement_to_terms_of_use.model.view_model.AgreementToTermsOfUseViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setAgreementToTermsofUseViewModel(@Nullable com.moaPlatform.moa.agreement_to_terms_of_use.model.view_model.AgreementToTermsOfUseViewModel AgreementToTermsofUseViewModel) {
        this.mAgreementToTermsofUseViewModel = AgreementToTermsofUseViewModel;
        synchronized(this) {
            mDirtyFlags |= 0x8L;
        }
        notifyPropertyChanged(BR.agreementToTermsofUseViewModel);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeAgreementToTermsofUseViewModelAllAgree((androidx.lifecycle.MutableLiveData<java.lang.Boolean>) object, fieldId);
            case 1 :
                return onChangeAgreementToTermsofUseViewModelEventAgree((androidx.lifecycle.MutableLiveData<java.lang.Boolean>) object, fieldId);
            case 2 :
                return onChangeAgreementToTermsofUseViewModelGpsAgree((androidx.lifecycle.MutableLiveData<java.lang.Boolean>) object, fieldId);
        }
        return false;
    }
    private boolean onChangeAgreementToTermsofUseViewModelAllAgree(androidx.lifecycle.MutableLiveData<java.lang.Boolean> AgreementToTermsofUseViewModelAllAgree, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeAgreementToTermsofUseViewModelEventAgree(androidx.lifecycle.MutableLiveData<java.lang.Boolean> AgreementToTermsofUseViewModelEventAgree, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeAgreementToTermsofUseViewModelGpsAgree(androidx.lifecycle.MutableLiveData<java.lang.Boolean> AgreementToTermsofUseViewModelGpsAgree, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x4L;
            }
            return true;
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        boolean customSafeBoxUnBoxAgreementToTermsofUseViewModelGpsAgree = false;
        androidx.lifecycle.MutableLiveData<java.lang.Boolean> agreementToTermsofUseViewModelAllAgree = null;
        java.lang.Boolean agreementToTermsofUseViewModelEventAgreeGetValue = null;
        com.moaPlatform.moa.agreement_to_terms_of_use.model.view_model.AgreementToTermsOfUseViewModel agreementToTermsofUseViewModel = mAgreementToTermsofUseViewModel;
        boolean customSafeBoxUnBoxAgreementToTermsofUseViewModelAllAgree = false;
        java.lang.Boolean agreementToTermsofUseViewModelAllAgreeGetValue = null;
        androidx.lifecycle.MutableLiveData<java.lang.Boolean> agreementToTermsofUseViewModelEventAgree = null;
        boolean customSafeBoxUnBoxAgreementToTermsofUseViewModelEventAgree = false;
        androidx.lifecycle.MutableLiveData<java.lang.Boolean> agreementToTermsofUseViewModelGpsAgree = null;
        java.lang.Boolean agreementToTermsofUseViewModelGpsAgreeGetValue = null;

        if ((dirtyFlags & 0x1fL) != 0) {


            if ((dirtyFlags & 0x19L) != 0) {

                    if (agreementToTermsofUseViewModel != null) {
                        // read agreementToTermsofUseViewModel.allAgree
                        agreementToTermsofUseViewModelAllAgree = agreementToTermsofUseViewModel.allAgree;
                    }
                    updateLiveDataRegistration(0, agreementToTermsofUseViewModelAllAgree);


                    if (agreementToTermsofUseViewModelAllAgree != null) {
                        // read agreementToTermsofUseViewModel.allAgree.getValue()
                        agreementToTermsofUseViewModelAllAgreeGetValue = agreementToTermsofUseViewModelAllAgree.getValue();
                    }


                    // read CustomSafeBox.unBox(agreementToTermsofUseViewModel.allAgree.getValue())
                    customSafeBoxUnBoxAgreementToTermsofUseViewModelAllAgree = com.moaPlatform.moa.util.data_binding.CustomSafeBox.unBox(agreementToTermsofUseViewModelAllAgreeGetValue);
            }
            if ((dirtyFlags & 0x1aL) != 0) {

                    if (agreementToTermsofUseViewModel != null) {
                        // read agreementToTermsofUseViewModel.eventAgree
                        agreementToTermsofUseViewModelEventAgree = agreementToTermsofUseViewModel.eventAgree;
                    }
                    updateLiveDataRegistration(1, agreementToTermsofUseViewModelEventAgree);


                    if (agreementToTermsofUseViewModelEventAgree != null) {
                        // read agreementToTermsofUseViewModel.eventAgree.getValue()
                        agreementToTermsofUseViewModelEventAgreeGetValue = agreementToTermsofUseViewModelEventAgree.getValue();
                    }


                    // read CustomSafeBox.unBox(agreementToTermsofUseViewModel.eventAgree.getValue())
                    customSafeBoxUnBoxAgreementToTermsofUseViewModelEventAgree = com.moaPlatform.moa.util.data_binding.CustomSafeBox.unBox(agreementToTermsofUseViewModelEventAgreeGetValue);
            }
            if ((dirtyFlags & 0x1cL) != 0) {

                    if (agreementToTermsofUseViewModel != null) {
                        // read agreementToTermsofUseViewModel.gpsAgree
                        agreementToTermsofUseViewModelGpsAgree = agreementToTermsofUseViewModel.gpsAgree;
                    }
                    updateLiveDataRegistration(2, agreementToTermsofUseViewModelGpsAgree);


                    if (agreementToTermsofUseViewModelGpsAgree != null) {
                        // read agreementToTermsofUseViewModel.gpsAgree.getValue()
                        agreementToTermsofUseViewModelGpsAgreeGetValue = agreementToTermsofUseViewModelGpsAgree.getValue();
                    }


                    // read CustomSafeBox.unBox(agreementToTermsofUseViewModel.gpsAgree.getValue())
                    customSafeBoxUnBoxAgreementToTermsofUseViewModelGpsAgree = com.moaPlatform.moa.util.data_binding.CustomSafeBox.unBox(agreementToTermsofUseViewModelGpsAgreeGetValue);
            }
        }
        // batch finished
        if ((dirtyFlags & 0x19L) != 0) {
            // api target 1

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked(this.allCheckBox, customSafeBoxUnBoxAgreementToTermsofUseViewModelAllAgree);
        }
        if ((dirtyFlags & 0x10L) != 0) {
            // api target 1

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setListeners(this.allCheckBox, mCallback1, allCheckBoxandroidCheckedAttrChanged);
            androidx.databinding.adapters.CompoundButtonBindingAdapter.setListeners(this.checkBoxEventAgree, mCallback3, checkBoxEventAgreeandroidCheckedAttrChanged);
            androidx.databinding.adapters.CompoundButtonBindingAdapter.setListeners(this.checkBoxGpsAgree, mCallback2, checkBoxGpsAgreeandroidCheckedAttrChanged);
        }
        if ((dirtyFlags & 0x1aL) != 0) {
            // api target 1

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked(this.checkBoxEventAgree, customSafeBoxUnBoxAgreementToTermsofUseViewModelEventAgree);
        }
        if ((dirtyFlags & 0x1cL) != 0) {
            // api target 1

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked(this.checkBoxGpsAgree, customSafeBoxUnBoxAgreementToTermsofUseViewModelGpsAgree);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnCheckedChanged(int sourceId , android.widget.CompoundButton callbackArg_0, boolean callbackArg_1) {
        switch(sourceId) {
            case 2: {
                // localize variables for thread safety
                // agreementToTermsofUseViewModel
                com.moaPlatform.moa.agreement_to_terms_of_use.model.view_model.AgreementToTermsOfUseViewModel agreementToTermsofUseViewModel = mAgreementToTermsofUseViewModel;
                // agreementToTermsofUseViewModel != null
                boolean agreementToTermsofUseViewModelJavaLangObjectNull = false;



                agreementToTermsofUseViewModelJavaLangObjectNull = (agreementToTermsofUseViewModel) != (null);
                if (agreementToTermsofUseViewModelJavaLangObjectNull) {




                    agreementToTermsofUseViewModel.onCheckChange(callbackArg_0, callbackArg_1);
                }
                break;
            }
            case 3: {
                // localize variables for thread safety
                // agreementToTermsofUseViewModel
                com.moaPlatform.moa.agreement_to_terms_of_use.model.view_model.AgreementToTermsOfUseViewModel agreementToTermsofUseViewModel = mAgreementToTermsofUseViewModel;
                // agreementToTermsofUseViewModel != null
                boolean agreementToTermsofUseViewModelJavaLangObjectNull = false;



                agreementToTermsofUseViewModelJavaLangObjectNull = (agreementToTermsofUseViewModel) != (null);
                if (agreementToTermsofUseViewModelJavaLangObjectNull) {




                    agreementToTermsofUseViewModel.onCheckChange(callbackArg_0, callbackArg_1);
                }
                break;
            }
            case 1: {
                // localize variables for thread safety
                // agreementToTermsofUseViewModel
                com.moaPlatform.moa.agreement_to_terms_of_use.model.view_model.AgreementToTermsOfUseViewModel agreementToTermsofUseViewModel = mAgreementToTermsofUseViewModel;
                // agreementToTermsofUseViewModel != null
                boolean agreementToTermsofUseViewModelJavaLangObjectNull = false;



                agreementToTermsofUseViewModelJavaLangObjectNull = (agreementToTermsofUseViewModel) != (null);
                if (agreementToTermsofUseViewModelJavaLangObjectNull) {




                    agreementToTermsofUseViewModel.onCheckChange(callbackArg_0, callbackArg_1);
                }
                break;
            }
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): agreementToTermsofUseViewModel.allAgree
        flag 1 (0x2L): agreementToTermsofUseViewModel.eventAgree
        flag 2 (0x3L): agreementToTermsofUseViewModel.gpsAgree
        flag 3 (0x4L): agreementToTermsofUseViewModel
        flag 4 (0x5L): null
    flag mapping end*/
    //end
}