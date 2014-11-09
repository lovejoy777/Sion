package com.sion.lovejoy777sa.sion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import util.IabHelper;
import util.IabResult;
import util.Inventory;
import util.Purchase;


public class InAppBillingCoffee extends Activity {

    IabHelper mHelper;
    static final String ITEM_SKU1 = "coffee1";
    static final String ITEM_SKU2 = "coffee2";
    static final String ITEM_SKU3 = "coffee3";
    static final String ITEM_SKU4 = "coffee4";


    private Button clickButton1, clickButton2, clickButton3, clickButton4;
    private Button donBtn1, donBtn2, donBtn3, donBtn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoadPrefs();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inappbillingcoffee);

        donBtn1 = (Button) findViewById(R.id.donBtn1);
        donBtn2 = (Button) findViewById(R.id.donBtn2);
        donBtn3 = (Button) findViewById(R.id.donBtn3);
        donBtn4 = (Button) findViewById(R.id.donBtn4);
        clickButton1 = (Button) findViewById(R.id.clickButton1);
        clickButton2 = (Button) findViewById(R.id.clickButton2);
        clickButton3 = (Button) findViewById(R.id.clickButton3);
        clickButton4 = (Button) findViewById(R.id.clickButton4);
        clickButton1.setEnabled(false);
        clickButton2.setEnabled(false);
        clickButton3.setEnabled(false);
        clickButton4.setEnabled(false);


        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAi1j8DqmMsSsQ8QYZ5RJAIaZQAOj+Jj86WqNnmQrKytKfknloczQBx3gHatDGcxyiXgpojeJpg3mtsgV/Cpc0nDpR3lEBfRj8U4UyJMmodWVeWRjX91N0jF2iUY20vS8WzmDHfDdumQMTPEbTE3Gd6XL8iWhsC4dhqefJhmcAz06vuuIJplsSxwZy7iYOwFUY5DiGfoN+EZuQsoGRXSHKymgoXrTJ1aFHZQwxui79XIgXEGIYO2l61wuol9BFCbv/T6jhcsOe35GxlkYwE2+59fvWMdDj12EZhYy36q9PBZRD66zaxYQXq9j4mFtW5gaij6pUUDz7hLg5egC99t5w2QIDAQAB";

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new
                                   IabHelper.OnIabSetupFinishedListener() {
                                       public void onIabSetupFinished(IabResult result) {

                                       }
                                   });


    }

    public void buttonClicked1(View view) {
        startActivity(new Intent("com.sion.lovejoy777sa.sion.THANKS"));
        clickButton1.setEnabled(false);
        donBtn1.setEnabled(true);
    }

    public void buttonClicked2(View view) {
        startActivity(new Intent("com.sion.lovejoy777sa.sion.THANKS"));
        clickButton2.setEnabled(false);
        donBtn2.setEnabled(true);
    }

    public void buttonClicked3(View view) {
        startActivity(new Intent("com.sion.lovejoy777sa.sion.THANKS"));
        clickButton3.setEnabled(false);
        donBtn3.setEnabled(true);
    }

    public void buttonClicked4(View view) {
        startActivity(new Intent("com.sion.lovejoy777sa.sion.THANKS"));
        clickButton4.setEnabled(false);
        donBtn4.setEnabled(true);
    }

    public void buyClick1(View view) {
        mHelper.launchPurchaseFlow(this, ITEM_SKU1, 10001,
                mPurchaseFinishedListener, "");
    }

    public void buyClick2(View view) {
        mHelper.launchPurchaseFlow(this, ITEM_SKU2, 10002,
                mPurchaseFinishedListener, "");
    }

    public void buyClick3(View view) {
        mHelper.launchPurchaseFlow(this, ITEM_SKU3, 10003,
                mPurchaseFinishedListener, "");
    }

    public void buyClick4(View view) {
        mHelper.launchPurchaseFlow(this, ITEM_SKU4, 10004,
                mPurchaseFinishedListener, "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase) {
            if (result.isFailure()) {
                // Handle error
                return;
            } else if (purchase.getSku().equals(ITEM_SKU1)) {
                consumeItem();
                donBtn1.setEnabled(false);
            } else if (purchase.getSku().equals(ITEM_SKU2)) {
                consumeItem2();
                donBtn2.setEnabled(false);
            } else if (purchase.getSku().equals(ITEM_SKU3)) {
                consumeItem3();
                donBtn3.setEnabled(false);
            } else if (purchase.getSku().equals(ITEM_SKU4)) {
                consumeItem4();
                donBtn4.setEnabled(false);
            }


        }
    };

    public void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    public void consumeItem2() {
        mHelper.queryInventoryAsync(m2ReceivedInventoryListener);
    }

    public void consumeItem3() {
        mHelper.queryInventoryAsync(m3ReceivedInventoryListener);
    }

    public void consumeItem4() {
        mHelper.queryInventoryAsync(m4ReceivedInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {


            if (result.isFailure()) {
                // Handle failure
            } else {
                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU1),
                        mConsumeFinishedListener);
            }

        }
    };

    IabHelper.QueryInventoryFinishedListener m2ReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {


            if (result.isFailure()) {
                // Handle failure
            } else {
                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU2),
                        m2ConsumeFinishedListener);
            }

        }
    };

    IabHelper.QueryInventoryFinishedListener m3ReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {


            if (result.isFailure()) {
                // Handle failure
            } else {
                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU3),
                        m3ConsumeFinishedListener);
            }

        }
    };

    IabHelper.QueryInventoryFinishedListener m4ReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {


            if (result.isFailure()) {
                // Handle failure
            } else {
                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU4),
                        m4ConsumeFinishedListener);
            }

        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
                        clickButton1.setEnabled(true);
                    } else {
                        // handle error
                    }
                }
            };

    IabHelper.OnConsumeFinishedListener m2ConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
                        clickButton2.setEnabled(true);
                    } else {
                        // handle error
                    }
                }
            };

    IabHelper.OnConsumeFinishedListener m3ConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
                        clickButton3.setEnabled(true);
                    } else {
                        // handle error
                    }
                }
            };

    IabHelper.OnConsumeFinishedListener m4ConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
                        clickButton4.setEnabled(true);
                    } else {
                        // handle error
                    }
                }
            };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    private void LoadPrefs() {
        //cb = (CheckBox) findViewById(R.id.checkBoxDark);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean cbValue = sp.getBoolean("CHECKBOX", false);
        if(cbValue){
            setTheme(R.style.DarkTheme);

        }else{
            setTheme(R.style.LightTheme);

        }


    }
}

