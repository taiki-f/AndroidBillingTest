package jp.domain.apps.billingtest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;

import jp.ikippe.apps.localtest.R;
import jp.domain.libs.billing.*;
import jp.domain.libs.billing.item.*;
import jp.domain.libs.billing.result.*;

import java.util.ArrayList;
import java.util.List;

/**
 * メインアクティビティ
 */
public class MainActivity extends AppCompatActivity {

    // 購入アイテムリスト
    private List<PurchaseItem> purchaseItemList;

    // 課金システムのラッパー
    private BillingConnectWrapper billingWrapper;

    // 状況確認用のテキストビューリスト
    private List<TextView> itemTextViewsList;

    // 購入ボタンリスト
    private List<Button> purchaseButtonList;
    private Button restoreButton;
    private Button queryButton;

    /**
     * アクティビティの開始時
     *
     * @param savedInstanceState インスタンスステート
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 課金の初期化
        purchaseInit();

        // ビューの初期化
        initView();
    }

    /**
     * アプリ終了時
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 課金システムを切断
        billingWrapper.unbind();
    }

    /**
     * レジューム
     */
    @Override
    protected void onResume() {
        super.onResume();

        // 購入途中のアイテムがある場合の復元処理(ここだと課金の終了毎にも呼ばれるのでonCreate経由だけに集約)
        //billingWrapper.queryPurchases(new SimpleResult());
    }

    /**
     * 課金の初期化
     */
    protected void purchaseInit() {
        // 購入アイテムリストの準備
        purchaseItemList = new ArrayList<>();
        purchaseItemList.add(new PurchaseItem(new PurchaseResult(), "item01"));
        purchaseItemList.add(new PurchaseItem(new PurchaseResult(), "item02"));
        purchaseItemList.add(new PurchaseItem(new PurchaseResult(), "sound_pack"));

        // 課金システムの準備
        billingWrapper = new BillingConnectWrapper(this, purchaseItemList);
        billingWrapper.bind(new BindResult());
    }

    /**
     * ビューの初期化
     */
    protected void initView() {
        // 状況確認用のビューを準備
        itemTextViewsList = new ArrayList<>();
        itemTextViewsList.add((TextView)findViewById(R.id.itemTextView00));
        itemTextViewsList.add((TextView)findViewById(R.id.itemTextView01));
        itemTextViewsList.add((TextView)findViewById(R.id.itemTextView02));
        for (TextView view : itemTextViewsList)
        {
            view.setText("itemId : ");
        }

        // 購入ボタンリストの準備
        purchaseButtonList = new ArrayList<>();
        purchaseButtonList.add((Button)findViewById(R.id.purchaseButton00));
        purchaseButtonList.add((Button)findViewById(R.id.purchaseButton01));
        purchaseButtonList.add((Button)findViewById(R.id.purchaseButton02));
        purchaseButtonList.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBillingFlowItem00();
            }
        });
        purchaseButtonList.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBillingFlowItem01();
            }
        });
        purchaseButtonList.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBillingFlowItem02();
            }
        });

        // リストアボタンの準備
        restoreButton = findViewById(R.id.restoreButton);
        restoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restorePurchaseItems();
            }
        });

        // 購入復帰ボタンの準備
        queryButton = findViewById(R.id.queryButton);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IBillingResult billingResult = new SimpleResult();
                billingWrapper.queryPurchases(billingResult);
            }
        });
    }

    /**
     * 購入アイテムのリストア
     */
    protected void restorePurchaseItems() {
        billingWrapper.restorePurchaseItem(new RestoreResult());
    }

    /**
     * 課金アイテムの購入処理
     */
    protected void startBillingFlowItem00() {
        PurchaseItem item = purchaseItemList.get(0);
        billingWrapper.startBillingFlow(new SimpleResult(), item.getSkuDetails());
    }
    protected void startBillingFlowItem01() {
        PurchaseItem item = purchaseItemList.get(1);
        billingWrapper.startBillingFlow(new SimpleResult(), item.getSkuDetails());
    }
    protected void startBillingFlowItem02() {
        PurchaseItem item = purchaseItemList.get(2);
        billingWrapper.startBillingFlow(new SimpleResult(), item.getSkuDetails());
    }

    // バインドリザルト
    class BindResult implements IBillingResult {
        @Override
        public void resultNotify(@NonNull BillingResult billingResult) {
            billingWrapper.queryPurchases(new RestoreResult());
        }
    }

    // リストアリザルト
    class RestoreResult implements IBillingResult {
        @Override
        public void resultNotify(@NonNull BillingResult billingResult) {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                Log.d("appBillingTest", "リストアの結果の取得に成功");
                for (int i = 0; i < purchaseItemList.size(); ++i) {
                    TextView view = itemTextViewsList.get(i);
                    PurchaseItem item = purchaseItemList.get(i);
                    StringBuilder sb = new StringBuilder();
                    sb.append("ItemInfo Num : ");
                    sb.append(i);
                    sb.append("\n");
                    sb.append("Name : ");
                    sb.append(item.getItemName());
                    sb.append("\n");
                    sb.append("Price : ");
                    sb.append(item.getSkuDetails().getPrice());
                    sb.append("\n");
                    sb.append("purchased : ");
                    sb.append(item.IsPurchased() ? "YES" : "NO");
                    sb.append("\n");

                    view.setText(sb.toString());
                }
            }
            else {
                Log.d("appBillingTest", "リストアの結果の取得に失敗 [" + billingResult.getResponseCode() + "]");
            }
        }
    }
}

/**
 * 課金リザルト
 */
class PurchaseResult implements IBillingResult {

    /**
     * 結果を通知
     *
     * @param billingResult 課金結果
     */
    @Override
    public void resultNotify(@NonNull BillingResult billingResult) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            Log.d("billing", "購入成功");

            // アイテム情報の反映処理を書く
            // 購入完了ダイアログを出す
        }
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Log.d("billing", "ユーザーによってキャンセル");

            // 購入キャンセルダイアログを出す
        }
        else {
            Log.d("billing", "上記以外のエラー");

            // エラーダイアログを出す
        }
    }
}


/**
 * シンプルなリザルト
 */
class SimpleResult implements IBillingResult {

    /**
     * 結果を通知
     *
     * @param billingResult 課金結果
     */
    @Override
    public void resultNotify(@NonNull BillingResult billingResult) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            Log.i("appBillingTest", "課金通信成功");
        }
    }
}
