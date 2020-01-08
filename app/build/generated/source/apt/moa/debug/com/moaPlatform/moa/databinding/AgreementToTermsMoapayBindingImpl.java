package com.moaPlatform.moa.databinding;
import com.moaPlatform.moa.R;
import com.moaPlatform.moa.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class AgreementToTermsMoapayBindingImpl extends AgreementToTermsMoapayBinding implements com.moaPlatform.moa.generated.callback.OnCheckedChangeListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.commontitle, 6);
        sViewsWithIds.put(R.id.scrollViews, 7);
        sViewsWithIds.put(R.id.headForm, 8);
        sViewsWithIds.put(R.id.txtAgreeStart, 9);
        sViewsWithIds.put(R.id.pay_middle_line, 10);
        sViewsWithIds.put(R.id.pay_middle_line_board, 11);
        sViewsWithIds.put(R.id.termsAndCondition, 12);
        sViewsWithIds.put(R.id.demoLine, 13);
        sViewsWithIds.put(R.id.textViewAgree, 14);
        sViewsWithIds.put(R.id.textViewAgree2, 15);
        sViewsWithIds.put(R.id.textViewAgree3, 16);
        sViewsWithIds.put(R.id.textViewAgree4, 17);
        sViewsWithIds.put(R.id.gonextpage, 18);
    }
    // views
    // variables
    @Nullable
    private final android.widget.CompoundButton.OnCheckedChangeListener mCallback8;
    @Nullable
    private final android.widget.CompoundButton.OnCheckedChangeListener mCallback6;
    @Nullable
    private final android.widget.CompoundButton.OnCheckedChangeListener mCallback4;
    @Nullable
    private final android.widget.CompoundButton.OnCheckedChangeListener mCallback7;
    @Nullable
    private final android.widget.CompoundButton.OnCheckedChangeListener mCallback5;
    // values
    // listeners
    // Inverse Binding Event Handlers
    private androidx.databinding.InverseBindingListener allCheckBoxsandroidCheckedAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of CustomSafeBox.unBox(agreementToTermsofUseViewModel.allAgree.getValue())
            //         is agreementToTermsofUseViewModel.allAgree.setValue((java.lang.Boolean) CustomSafeBox.boxBoolean(callbackArg_0))
            boolean callbackArg_0 = allCheckBoxs.isChecked();
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
    private androidx.databinding.InverseBindingListener checkBoxAgreeandroidCheckedAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of CustomSafeBox.unBox(agreementToTermsofUseViewModel.gpsAgree.getValue())
            //         is agreementToTermsofUseViewModel.gpsAgree.setValue((java.lang.Boolean) CustomSafeBox.boxBoolean(callbackArg_0))
            boolean callbackArg_0 = checkBoxAgree.isChecked();
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
    private androidx.databinding.InverseBindingListener checkBoxAgree2androidCheckedAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of CustomSafeBox.unBox(agreementToTermsofUseViewModel.eventAgree.getValue())
            //         is agreementToTermsofUseViewModel.eventAgree.setValue((java.lang.Boolean) CustomSafeBox.boxBoolean(callbackArg_0))
            boolean callbackArg_0 = checkBoxAgree2.isChecked();
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
    private androidx.databinding.InverseBindingListener checkBoxAgree3androidCheckedAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of CustomSafeBox.unBox(agreementToTermsofUseViewModel.eventAgree.getValue())
            //         is agreementToTermsofUseViewModel.eventAgree.setValue((java.lang.Boolean) CustomSafeBox.boxBoolean(callbackArg_0))
            boolean callbackArg_0 = checkBoxAgree3.isChecked();
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
    private androidx.databinding.InverseBindingListener checkBoxAgree4androidCheckedAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of CustomSafeBox.unBox(agreementToTermsofUseViewModel.eventAgree.getValue())
            //         is agreementToTermsofUseViewModel.eventAgree.setValue((java.lang.Boolean) CustomSafeBox.boxBoolean(callbackArg_0))
            boolean callbackArg_0 = checkBoxAgree4.isChecked();
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

    public AgreementToTermsMoapayBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 19, sIncludes, sViewsWithIds));
    }
    private AgreementToTermsMoapayBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 3
            , (android.widget.CheckBox) bindings[1]
            , (android.widget.CheckBox) bindings[2]
            , (android.widget.CheckBox) bindings[3]
            , (android.widget.CheckBox) bindings[4]
            , (android.widget.CheckBox) bindings[5]
            , (com.moaPlatform.moa.util.custom_view.CommonTitleView) bindings[6]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[0]
            , (android.widget.ImageView) bindings[13]
            , (android.widget.Button) bindings[18]
            , (android.widget.LinearLayout) bindings[8]
            , (android.view.View) bindings[10]
            , (android.view.View) bindings[11]
            , (android.widget.ScrollView) bindings[7]
            , (android.widget.TextView) bindings[12]
            , (android.widget.TextView) bindings[14]
            , (android.widget.TextView) bindings[15]
            , (android.widget.TextView) bindings[16]
            , (android.widget.TextView) bindings[17]
            , (android.widget.TextView) bindings[9]
            );
        this.allCheckBoxs.setTag(null);
        this.checkBoxAgree.setTag(null);
        this.checkBoxAgree2.setTag(null);
        this.checkBoxAgree3.setTag(null);
        this.checkBoxAgree4.setTag(null);
        this.constop.setTag(null);
        setRootTag(root);
        // listeners
        mCallback8 = new com.moaPlatform.moa.generated.callback.OnCheckedChangeListener(this, 5);
        mCallback6 = new com.moaPlatform.moa.generated.callback.OnCheckedChangeListener(this, 3);
        mCallback4 = new com.moaPlatform.moa.generated.callback.OnCheckedChangeListener(this, 1);
        mCallback7 = new com.moaPlatform.moa.generated.callback.OnCheckedChangeListener(this, 4);
        mCallback5 = new com.moaPlatform.moa.generated.callback.OnCheckedChangeListener(this, 2);
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

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked(this.allCheckBoxs, customSafeBoxUnBoxAgreementToTermsofUseViewModelAllAgree);
        }
        if ((dirtyFlags & 0x10L) != 0) {
            // api target 1

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setListeners(this.allCheckBoxs, mCallback4, allCheckBoxsandroidCheckedAttrChanged);
            androidx.databinding.adapters.CompoundButtonBindingAdapter.setListeners(this.checkBoxAgree, mCallback5, checkBoxAgreeandroidCheckedAttrChanged);
            androidx.databinding.adapters.CompoundButtonBindingAdapter.setListeners(this.checkBoxAgree2, mCallback6, checkBoxAgree2androidCheckedAttrChanged);
            androidx.databinding.adapters.CompoundButtonBindingAdapter.setListeners(this.checkBoxAgree3, mCallback7, checkBoxAgree3androidCheckedAttrChanged);
            androidx.databinding.adapters.CompoundButtonBindingAdapter.setListeners(this.checkBoxAgree4, mCallback8, checkBoxAgree4androidCheckedAttrChanged);
        }
        if ((dirtyFlags & 0x1cL) != 0) {
            // api target 1

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked(this.checkBoxAgree, customSafeBoxUnBoxAgreementToTermsofUseViewModelGpsAgree);
        }
        if ((dirtyFlags & 0x1aL) != 0) {
            // api target 1

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked(this.checkBoxAgree2, customSafeBoxUnBoxAgreementToTermsofUseViewModelEventAgree);
            androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked(this.checkBoxAgree3, customSafeBoxUnBoxAgreementToTermsofUseViewModelEventAgree);
            androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked(this.checkBoxAgree4, customSafeBoxUnBoxAgreementToTermsofUseViewModelEventAgree);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnCheckedChanged(int sourceId , android.widget.CompoundButton callbackArg_0, boolean callbackArg_1) {
        switch(sourceId) {
            case 5: {
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
            case 4: {
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