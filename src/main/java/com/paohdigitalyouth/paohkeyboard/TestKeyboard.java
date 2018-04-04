package com.paohdigitalyouth.paohkeyboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.widget.PopupWindow;
import android.widget.Toast;

import nnl.keyboard.EmojiInput;


public class TestKeyboard extends InputMethodService implements OnKeyboardActionListener{

    private KeyboardView kv;
    private Keyboard keyboardQwerty;
    private Keyboard keyboardQwerty2;
    private Keyboard keyboardSymbol;
    private Keyboard keyboardSymbol2;
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
    boolean fixMe = false;

    SharedPreferences sharedPreferences;

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        if (ic==null){
            return;
        }
        fixMe = !(kv.getKeyboard() == keyboardSymbol || kv.getKeyboard() == keyboardSymbol2 || kv.getKeyboard() == keyboardNumber || kv.getKeyboard() == keyboardPhone);
        playClick(primaryCode);
        switch(primaryCode){

            case Keyboard.KEYCODE_DELETE ://for delete key
                fixMe = false;
                ic.deleteSurroundingText(1,0);
            ic.commitText("",1);
                break;

            case Keyboard.KEYCODE_SHIFT://for eng shift key
                caps = !caps;
                keyboardCurrent.setShifted(caps);
                kv.invalidateAllKeys();
                break;

            //copy,cut,select all
            case -200:
                fixMe = false;
                ic.performContextMenuAction(android.R.id.selectAll);
                break;
            case -201:
                fixMe = false;
                ic.performContextMenuAction(android.R.id.copy);
                break;
            case -202:
                fixMe = false;
                ic.performContextMenuAction(android.R.id.cut);
                break;
            case -203:
                fixMe = false;
                ic.performContextMenuAction(android.R.id.paste);
                break;

            //left,right arrow
            case 2400: //For Left Arrow]
                fixMe = false;
                long now1 = System.currentTimeMillis();
                ic.sendKeyEvent(new KeyEvent( now1, now1, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT, 0, 0));
                ic.sendKeyEvent(new KeyEvent( now1, now1, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_LEFT, 0, 0));
                break;

            case 2401://For Right Arrow
                fixMe = false;
                long now2 = System.currentTimeMillis();
                ic.sendKeyEvent(new KeyEvent( now2, now2, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT, 0, 0));
                ic.sendKeyEvent(new KeyEvent( now2, now2, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_RIGHT, 0, 0));
                break;

            case 2402://For Up Arrow
                fixMe = false;
                long now3 = System.currentTimeMillis();
                ic.sendKeyEvent(new KeyEvent( now3, now3, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_UP, 0, 0));
                ic.sendKeyEvent(new KeyEvent( now3, now3, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_UP, 0, 0));
                break;

            case 2403://For Down Arrow
                fixMe = false;
                long now4 = System.currentTimeMillis();
                ic.sendKeyEvent(new KeyEvent( now4, now4, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN, 0, 0));
                ic.sendKeyEvent(new KeyEvent( now4, now4, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_DOWN, 0, 0));
                break;

            //for popupkeyboard
            case 2301:
                fixMe = false;
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
                fixMe = false;
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
                fixMe = false;
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
                fixMe = false;
               try {
                   popwd1.dismiss();
               }catch (Exception e){
               }

                break;

            //for Emoji lab
            case 8888:
                fixMe = false;
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
                fixMe = false;
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
                fixMe = false;
                ChangeKeyboard(keyboardQwerty);
                break;

			/*case -102:
				ChangeKeyboard(keyboardQwerty2);
				break;*/

            case -102:
                fixMe = false;
                mmshifted= true;
                ChangeKeyboard(keyboardQwerty2);
                break;

            case -103: //change Eng Keyboard
                fixMe = false;
                ChangeKeyboard(keyboardSymbol);
                break;

            case -104:
                fixMe = false;
                ChangeKeyboard(keyboardSymbol2);
                break;

            case -105: //hide Popup keyboard
                fixMe = false;
                requestHideSelf(0);
                break;


            case 20000://parli key
                fixMe = false;
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
                    Toast.makeText(this, "Change Theme Function!", Toast.LENGTH_SHORT).show();
                break;

            case 4145:
                fixMe = false;
                ic.commitText(String.valueOf((char)4145),1);
                break;

            case 4155:
                fixMe = false;
                ic.commitText(String.valueOf((char)4155),1);
                break;

                //fix Yaycha
            case 4140:
                String myString = "";
                try {
                    CharSequence currentText = ic.getExtractedText(new ExtractedTextRequest(), 0).text;
                    myString = currentText.toString();
                    boolean ogay = true;
                    int lengt;
                    if (myString.length()==5){
                        lengt = 5;
                    }else if (myString.length()==4){
                        lengt = 4;
                    }else if (myString.length()==3){
                        lengt = 3;
                    }else if (myString.length()==2){
                        lengt = 2;
                    }else{
                        lengt = myString.length();
                    }

                    if (lengt>=2) {
                        Log.d("Htetz","Lenght OK");
                        for (int i = 2; i < 5; i++) {
                            String yaYinz = myString.substring(myString.length() - i);
                            char yaYinzz = yaYinz.charAt(0);
                            if (yaYinzz == 4155 || yaYinzz == 4222 || yaYinzz == 4223 || yaYinzz == 4224 || yaYinzz == 4225 || yaYinzz == 4226 || yaYinzz == 4227 || yaYinzz == 4228) {
                                ic.commitText("ာ", 1);
                                ogay = false;
                                return;
                            }
                        }
                    }

                    if (myString.length()>=1){
                        Log.d("Htetz","Lenght2 OK");
                        char thaRa = myString.substring(myString.length() - 1).charAt(0);
                        if (ogay==true){
                            if (thaRa==4098||thaRa==4125||thaRa==4117||thaRa==4100||thaRa==4101||thaRa==4097||thaRa==4115||thaRa==4114){
                                ic.commitText("ါ",1);
                            }else{
                                ic.commitText("ာ",1);
                            }
                        }else{
                            ic.commitText("ာ",1);
                        }
                    }else{
                        ic.commitText("ာ",1);
                    }
                }catch (Exception e){
                    ic.commitText("ာ",1);
                }
                break;

            default:
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
                    ChangeKeyboard(keyboardQwerty);
                }
        }
        if (fixMe==true){
            fixMyanmarText(ic);
        }
    }

    private void fixMyanmarText(InputConnection ic) {
//        try {
//            CharSequence currentText = ic.getExtractedText(new ExtractedTextRequest(), 0).text;
//            if (currentText.length()==0){
//                return;
//            }else if (currentText.length()>=2){
////                CharSequence beforCursorText = ic.getTextBeforeCursor(currentText.length(), 0);
////                CharSequence afterCursorText = ic.getTextAfterCursor(currentText.length(), 0);
////                ic.deleteSurroundingText(beforCursorText.length(), afterCursorText.length());
//            String myString = currentText.toString();
//                myString = myString.substring(myString.length() - 2);
//                myString = Yone.zg2uni(myString);
//                myString = Yone.uni2zg(myString);
//                ic.deleteSurroundingText(2,0);
//                ic.commitText(myString,1);
////            if (myString.length() < 5) {
////                myString = myString;
////            } else if (myString.length() > 5) {
////                myString = myString.substring(myString.length() - 5);
////            }
////
////            myString = Yone.zg2uni(myString);
////            myString = Yone.uni2zg(myString);
//
////            ic.commitText("",myString.length());
////            ic.deleteSurroundingText(myString.length(), 0);
////            ic.commitText(myString,currentText.length()-myString.length());
//
////                currentText = Yone.zg2uni(currentText.toString());
////                currentText = Yone.uni2zg(currentText.toString());
////                ic.commitText(currentText, 1);
//            }
//        }catch (Exception e){
//
//        }
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
        keyboardQwerty = new Keyboard(this, R.xml.qwerty_paoh);
        keyboardQwerty2 = new Keyboard(this, R.xml.qwerty_paoh_shifted);
        keyboardSymbol = new Keyboard(this, R.xml.qwerty_eng);
        keyboardSymbol2 = new Keyboard(this, R.xml.symbol);
        keyboardPhone = new Keyboard(this, R.xml.phone);
        keyboardNumber = new Keyboard(this, R.xml.number);
        keyboardCurrent=keyboardSymbol;
        kv.setKeyboard(keyboardSymbol);
        kv.setOnKeyboardActionListener(this);
        return kv;
    }

    private void ChangeKeyboard(Keyboard nextKeyboard) {
        kv.setKeyboard(nextKeyboard);
        keyboardCurrent=nextKeyboard;
    }

    @Override //keyboard inti
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        boolean check = sharedPreferences.getBoolean("embedFont",true);
        if (check==true) {
            FontOverride.setDefaultFont(this, "DEFAULT", "fonts/paoh.ttf");
        }else{
            FontOverride.replaceFont("DEFAULT",Typeface.DEFAULT_BOLD);
        }

        Log.d("Htetz","onStartInput");
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
                        if(kv.getKeyboard()==keyboardSymbol) {
                            ChangeKeyboard(kv.getKeyboard());
                        }else if(kv.getKeyboard()==keyboardQwerty){
                            ChangeKeyboard(kv.getKeyboard());
                        }else if (kv.getKeyboard()==keyboardQwerty2){
                            ChangeKeyboard(kv.getKeyboard());
                        }else{
                            ChangeKeyboard(keyboardSymbol);
                        }
                    }else {
                        ChangeKeyboard(keyboardSymbol);
                    }
                }catch(Exception e){
                }
                break;

            default:
                try{
                    if (kv.getKeyboard()!=null){
                        if(kv.getKeyboard()==keyboardSymbol) {
                            ChangeKeyboard(kv.getKeyboard());
                        }else if(kv.getKeyboard()==keyboardQwerty){
                            ChangeKeyboard(kv.getKeyboard());
                        }else if (kv.getKeyboard()==keyboardQwerty2){
                            ChangeKeyboard(kv.getKeyboard());
                        }else{
                            ChangeKeyboard(keyboardSymbol);
                        }
                    }else {
                        ChangeKeyboard(keyboardSymbol);
                    }
                }catch(Exception e){

                }
        }
    }


    @Override //playClick method
    public void onCreate(){
        super.onCreate();
        sharedPreferences = getSharedPreferences("Htetz",MODE_PRIVATE);
        boolean check = sharedPreferences.getBoolean("embedFont",true);
        if (check==true) {
            FontOverride.setDefaultFont(this, "DEFAULT", "fonts/paoh.ttf");
        }else {
            FontOverride.replaceFont("DEFAULT",Typeface.DEFAULT_BOLD);
        }
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

    public void restart(){
        setInputView(onCreateInputView());
    }
}
