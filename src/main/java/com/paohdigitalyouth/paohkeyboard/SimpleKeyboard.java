package com.paohdigitalyouth.paohkeyboard;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.*;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.text.InputType;
import android.widget.PopupWindow;
import android.view.LayoutInflater;
import android.content.*;

import java.io.File;

import nnl.keyboard.EmojiInput;


public class SimpleKeyboard extends InputMethodService implements OnKeyboardActionListener{
    private KeyboardView kv;
    private Keyboard keyboardPaOh;
    private Keyboard keyboardPaOhShifted;
    private Keyboard keyboardEng;
    private Keyboard keyboardSymbol;
    private Keyboard keyboardCurrent;
    private Keyboard keyboardPhone;
    private Keyboard keyboardNumber;



    //Global
    PopupWindow popwd1;
    Keyboard popkb1;
    KeyboardView popkbv1;
    EmojiInput mEmojiInput;

    private boolean caps = false;
    boolean mmshifted=false;//for mm shift key
    boolean doubletype=false;//for parli
    SharedPreferences sharedPreferences;

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        Log.d("Htetz",String.valueOf(primaryCode));
        InputConnection ic = getCurrentInputConnection();
        if (ic==null){
            return;
        }
        playClick(primaryCode);
        switch(primaryCode){

            case Keyboard.KEYCODE_DELETE ://for delete key
                ic.deleteSurroundingText(1,0);
            ic.commitText("",1);
                doubletype = false;
                break;

            case Keyboard.KEYCODE_SHIFT://for eng shift key
                caps = !caps;
                keyboardCurrent.setShifted(caps);
                kv.invalidateAllKeys();
                break;

            //copy,cut,select all
            case -200:
                ic.performContextMenuAction(android.R.id.selectAll);
                break;
            case -201:
                ic.performContextMenuAction(android.R.id.copy);
                break;
            case -202:
                ic.performContextMenuAction(android.R.id.cut);
                break;
            case -203:
                ic.performContextMenuAction(android.R.id.paste);
                break;

            //left,right arrow
            case 2400: //For Left Arrow]
                long now1 = System.currentTimeMillis();
                ic.sendKeyEvent(new KeyEvent( now1, now1, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT, 0, 0));
                ic.sendKeyEvent(new KeyEvent( now1, now1, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_LEFT, 0, 0));
                break;

            case 2401://For Right Arrow
                long now2 = System.currentTimeMillis();
                ic.sendKeyEvent(new KeyEvent( now2, now2, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT, 0, 0));
                ic.sendKeyEvent(new KeyEvent( now2, now2, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_RIGHT, 0, 0));
                break;

            case 2402://For Up Arrow
                long now3 = System.currentTimeMillis();
                ic.sendKeyEvent(new KeyEvent( now3, now3, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_UP, 0, 0));
                ic.sendKeyEvent(new KeyEvent( now3, now3, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_UP, 0, 0));
                break;

            case 2403://For Down Arrow
                long now4 = System.currentTimeMillis();
                ic.sendKeyEvent(new KeyEvent( now4, now4, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN, 0, 0));
                ic.sendKeyEvent(new KeyEvent( now4, now4, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_DOWN, 0, 0));
                break;

            //for popupkeyboard
            case 2301:
                LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View container = inflater.inflate(R.layout.popupkb, null);
                popkb1 = new Keyboard(getApplicationContext(), R.xml.extra_popup);
                popwd1=new PopupWindow(getApplicationContext());
                popwd1.setBackgroundDrawable(null);

                popwd1.setContentView(container);
                popkbv1= container.findViewById(R.id.popupkb);
                popkbv1.setKeyboard(popkb1);
                popkbv1.setPopupParent(kv);
                popkbv1.setOnKeyboardActionListener(this);
                popwd1.setOutsideTouchable(false);
                popwd1.setWidth(kv.getWidth());
                popwd1.setHeight(kv.getHeight()); popwd1.showAtLocation(kv,17,0,0);
                break;

            case 2302://for eng num popup
                LayoutInflater inflater1 = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View container1 = inflater1.inflate(R.layout.popupkb, null);
                popkb1 = new Keyboard(getApplicationContext(), R.xml.engnumpopup);
                popwd1=new PopupWindow(getApplicationContext());
                popwd1.setBackgroundDrawable(null);

                popwd1.setContentView(container1);
                popkbv1= container1.findViewById(R.id.popupkb);
                popkbv1.setKeyboard(popkb1);
                popkbv1.setPopupParent(kv);
                popkbv1.setOnKeyboardActionListener(this);
                popwd1.setOutsideTouchable(false);
                popwd1.setWidth(kv.getWidth());
                popwd1.setHeight(kv.getHeight()); popwd1.showAtLocation(kv,17,0,0);

                break;

            case 2303://for mm num popup
                LayoutInflater inflater2 = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View container2 = inflater2.inflate(R.layout.popupkb, null);
                popkb1 = new Keyboard(getApplicationContext(), R.xml.paohnumpopup);
                popwd1=new PopupWindow(getApplicationContext());
                popwd1.setBackgroundDrawable(null);

                popwd1.setContentView(container2);
                popkbv1= container2.findViewById(R.id.popupkb);
                popkbv1.setKeyboard(popkb1);
                popkbv1.setPopupParent(kv);
                popkbv1.setOnKeyboardActionListener(this);
                popwd1.setOutsideTouchable(false);
                popwd1.setWidth(kv.getWidth());
                popwd1.setHeight(kv.getHeight()); popwd1.showAtLocation(kv,17,0,0);
                break;

            case 2300: // close popup keyboard
               try {
                   popwd1.dismiss();
               }catch (Exception e){
               }

                break;

            //for Emoji lab
            case 8888:

                mEmojiInput = new EmojiInput(this,kv); //kv က ကုိယ့္ရဲ့ main KeyboardView ျဖစ္ပါတယ္
                mEmojiInput.setOnKeyboardActionListener(this);
                popwd1=new PopupWindow(getApplicationContext());
                popwd1.setBackgroundDrawable(null);
                popwd1.setOutsideTouchable(false);
                popwd1.setWidth(kv.getWidth());
                popwd1.setHeight(kv.getHeight());
                popwd1.setContentView(mEmojiInput.getView());
                popwd1.showAtLocation(kv,17,0,0);
                break;

            case Keyboard.KEYCODE_DONE: //For Enter
                final int options = this.getCurrentInputEditorInfo().imeOptions;
                final int actionId = options & EditorInfo.IME_MASK_ACTION;
                switch (actionId) {
                    case EditorInfo.IME_ACTION_GO:
                        ic.performEditorAction(EditorInfo.IME_ACTION_GO);
                        break;
                    case EditorInfo.IME_ACTION_NEXT:
                        ic.performEditorAction(EditorInfo.IME_ACTION_NEXT);
                        break;

                    case EditorInfo.IME_ACTION_SEARCH:
                        ic.performEditorAction(EditorInfo.IME_ACTION_SEARCH);
                        break;

                    case EditorInfo.IME_ACTION_SEND:
                        ic.performEditorAction(EditorInfo.IME_ACTION_SEND);
                        break;

                    default:
                        ic.commitText("\n",1);
                        break;
                }
                break;


            case -101:
                ChangeKeyboard(keyboardPaOh);
                break;

			/*case -102:
				ChangeKeyboard(keyboardQwerty2);
				break;*/

            case -102:
                mmshifted= true;
                ChangeKeyboard(keyboardPaOhShifted);
                break;

            case -103: //change Eng Keyboard
                ChangeKeyboard(keyboardEng);
                break;

            case -104:
                ChangeKeyboard(keyboardSymbol);
                break;

            case -105: //hide Popup keyboard
                requestHideSelf(0);
                break;


            case 20000://parli key
                doubletype = true;
                break;

            case 4096:// For KaGyee
                if(doubletype){
                    ic.commitText("ၠ",1);
                    doubletype=false;
                } else{
                    ic.commitText("က",1);
                }
                break;

            case 4097:
                if(doubletype){
                    ic.commitText("ၡ",1);
                    doubletype=false;
                } else{
                    ic.commitText("ခ",1);
                }
                break;

            case 4098:
                if(doubletype){
                    ic.commitText("ၢ",1);
                    doubletype=false;
                } else{
                    ic.commitText("ဂ",1);
                }
                break;
            case 20004:
                if(doubletype){
                    ic.commitText("ၣ",1);
                    doubletype=false;
                } else{
                    ic.commitText("ဃ",1);
                }
                break;
            case 4101:
                if(doubletype){
                    ic.commitText("ၥ",1);
                    doubletype=false;
                } else{
                    ic.commitText("စ",1);
                }
                break;
            case 4102:
                if(doubletype){
                    ic.commitText("ၦ",1);
                    doubletype=false;
                } else{
                    ic.commitText("ဆ",1);
                }
                break;
            case 20008:
                if(doubletype){
                    ic.commitText("ၨ",1);
                    doubletype=false;
                } else{
                    ic.commitText("ဇ",1);
                }
                break;

            case 4111:
                if(doubletype){
                    ic.commitText("ၰ",1);
                    doubletype=false;
                } else{
                    ic.commitText("ဏ",1);
                }
                break;

            case 4112:
                if(doubletype){
                    ic.commitText("ၱ",1);
                    doubletype=false;
                } else{
                    ic.commitText("တ",1);
                }
                break;

            case 4113:
                if(doubletype){
                    ic.commitText("ၳ",1);
                    doubletype=false;
                } else{
                    ic.commitText("ထ",1);
                }
                break;

            case 4114:
                if(doubletype){
                    ic.commitText("ၵ",1);
                    doubletype=false;
                } else{
                    ic.commitText("ဒ",1);
                }
                break;

            case 4115:
                if(doubletype){
                    ic.commitText("ၶ",1);
                    doubletype=false;
                } else{
                    ic.commitText("ဓ",1);
                }
                break;

            case 4116:
                if(doubletype){
                    ic.commitText("ၷ",1);
                    doubletype=false;
                } else{
                    ic.commitText("န",1);
                }
                break;

            case 4117:
                if(doubletype){
                    ic.commitText("ၸ",1);
                    doubletype=false;
                } else{
                    ic.commitText("ပ",1);
                }
                break;

            case 4118:
                if(doubletype){
                    ic.commitText("ၹ",1);
                    doubletype=false;
                } else{
                    ic.commitText("ဖ",1);
                }
                break;

            case 4119:
                if(doubletype){
                    ic.commitText("ၺ",1);
                    doubletype=false;
                } else{
                    ic.commitText("ဗ",1);
                }
                break;

            case 4120:
                if(doubletype){
                    ic.commitText("ၻ",1);
                    doubletype=false;
                } else{
                    ic.commitText("ဘ",1);
                }
                break;

            case 4121:
                if(doubletype){
                    ic.commitText("ၼ",1);
                    doubletype=false;
                } else{
                    ic.commitText("မ",1);
                }
                break;

            case 4124:
                if(doubletype){
                    ic.commitText("ႅ",1);
                    doubletype=false;
                } else{
                    ic.commitText("လ",1);
                }
                break;

            case 4126:
                if(doubletype){
                    ic.commitText("ႆ",1);
                    doubletype=false;
                } else{
                    ic.commitText("သ",1);
                }
                break;

            case 2299:
                startActivity(new Intent(this,ThemeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case 4145:
                ic.commitText(String.valueOf((char)4145),1);
                break;

            case 4155:
                ic.commitText(String.valueOf((char)4155),1);
                break;

            default:
                doubletype = false;
                char code = (char)primaryCode;
                if(Character.isLetter(code) && caps){
                    code = Character.toUpperCase(code);
                }

                ic.commitText(String.valueOf(code), 1); // SMM

                if(caps){    // for eng shift key
                    caps = false;
                    keyboardCurrent.setShifted(false);
                    kv.invalidateAllKeys();
                }

                if(mmshifted){    //for mm shift key
                    mmshifted=false;
                    ChangeKeyboard(keyboardPaOh);
                }
        }
    }
    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
        Log.d("String","onText "+text);
        InputConnection ic = getCurrentInputConnection();
        ic.commitText(text, 1); // SMM
    }

    @Override
    public void swipeDown() {
        requestHideSelf(0);
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        //keyboard = new Keyboard(this, R.xml.qwerty);
        customChange();
        keyboardPaOh = new Keyboard(this, R.xml.qwerty_paoh);
        keyboardPaOhShifted = new Keyboard(this, R.xml.qwerty_paoh_shifted);
        keyboardEng = new Keyboard(this, R.xml.qwerty_eng);
        keyboardSymbol = new Keyboard(this, R.xml.symbol);
        keyboardPhone = new Keyboard(this, R.xml.phone);
        keyboardNumber = new Keyboard(this, R.xml.number);
        keyboardCurrent=keyboardEng;
        kv.setKeyboard(keyboardEng);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    private void ChangeKeyboard(Keyboard nextKeyboard) {
        kv.setKeyboard(nextKeyboard);
        keyboardCurrent=nextKeyboard;
    }

    @Override //keyboard inti
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        switch (attribute.inputType & InputType.TYPE_MASK_CLASS) {
            case InputType.TYPE_CLASS_PHONE:
                try{
                    ChangeKeyboard(keyboardPhone);
                }catch(Exception e){

                }
                break;
            case InputType.TYPE_CLASS_NUMBER:
                try{
                    ChangeKeyboard(keyboardNumber);
                }catch(Exception e){
                }
                break;

            case InputType.TYPE_CLASS_TEXT:
                try{
                    if (kv.getKeyboard()!=null){
                        if(kv.getKeyboard()==keyboardEng) {
                            ChangeKeyboard(kv.getKeyboard());
                        }else if(kv.getKeyboard()==keyboardPaOh){
                            ChangeKeyboard(kv.getKeyboard());
                        }else if (kv.getKeyboard()==keyboardPaOhShifted){
                            ChangeKeyboard(kv.getKeyboard());
                        }else{
                            ChangeKeyboard(keyboardEng);
                        }
                    }else {
                        ChangeKeyboard(keyboardEng);
                    }
                }catch(Exception e){
                }
                break;

            default:
                try{
                    if (kv.getKeyboard()!=null){
                        if(kv.getKeyboard()==keyboardEng) {
                            ChangeKeyboard(kv.getKeyboard());
                        }else if(kv.getKeyboard()==keyboardPaOh){
                            ChangeKeyboard(kv.getKeyboard());
                        }else if (kv.getKeyboard()==keyboardPaOhShifted){
                            ChangeKeyboard(kv.getKeyboard());
                        }else{
                            ChangeKeyboard(keyboardEng);
                        }
                    }else {
                        ChangeKeyboard(keyboardEng);
                    }
                }catch(Exception e){

                }
        }

        customChange();
    }

    public void customChange(){
        boolean check = sharedPreferences.getBoolean("embedFont",true);
        if (check==true) {
            FontOverride.setDefaultFont(this, "DEFAULT", "fonts/paoh.ttf");
        }else{
            FontOverride.resetDefaultFont("DEFAULT");
        }

        try {
            String theme = sharedPreferences.getString("theme","paoh");
            if(theme.equals("paoh")){
                kv.setBackgroundResource(R.drawable.paohflag);
            }else if (theme.equals("dark")){
                kv.setBackgroundColor(Color.BLACK);
            }else  if (theme.equals("image")){
                try {
                    File directory = new File(Environment.getExternalStorageDirectory()+"/Android/data/"+getPackageName()+"/");
                    File mypath=new File(directory,"htetz.png");
                    Drawable drawable = Drawable.createFromPath( mypath.toString());
                    kv.setBackgroundDrawable(drawable);
                }catch (Exception e){
                    kv.setBackgroundResource(R.drawable.paohflag);
                }
            }else{
                try {
                    kv.setBackgroundColor(Integer.parseInt(String.valueOf(theme)));
                }catch (Exception e){
                    kv.setBackgroundResource(R.drawable.paohflag);
                }
            }
        }catch (Exception e){

        }
    }

    @Override //playClick method
    public void onCreate(){
        super.onCreate();

        sharedPreferences = getSharedPreferences("Htetz",MODE_PRIVATE);
    }

    private void playClick(int keyCode){
        }


    @Override
    public void onFinishInputView(boolean finishingInput) {
        kv.closing();
        try {
            popwd1.dismiss();
        }catch (Exception e){
        }
        super.onFinishInputView(finishingInput);
    }

    @Override
    public void onFinishInput() {
        super.onFinishInput();
        Log.d("Htetz","onFinishInput");
        if (kv != null) {
            kv.closing();
        }
    }
}
