package com.example.basiccalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basiccalculator.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    String realValue = "";
    String keyValue = "";
    double result = 0;
    ArrayList<String> operationss = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setupKeys();
        binding.inputField.setText(realValue);
        binding.inputResult.setText("0");
    }

    private void setupKeys() {
        int[] buttonIds = {R.id.key_1, R.id.key_2, R.id.key_3, R.id.key_4, R.id.key_5, R.id.key_6, R.id.key_7, R.id.key_8, R.id.key_9, R.id.key_0
                , R.id.key_plus, R.id.key_minus, R.id.key_multiple, R.id.key_divide, R.id.key_changeValue, R.id.key_comma, R.id.key_Mod, R.id.key_Clear, R.id.key_delete, R.id.key_Result};

        for (int buttonId : buttonIds) {
            findViewById(buttonId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    realValue = binding.inputField.getText().toString();
                    binding.inputField.setText(realValue);
                    // Hocam this is for DELETE key.
                    // If length is 1, when deleting another keyword, its being 0
                    if (buttonId == R.id.key_delete) {
                        if (realValue.length() > 1) {
                            realValue = realValue.substring(0, realValue.length() - 1);
                            binding.inputField.setText(realValue);
                        } else if (realValue.length() == 1) {
                            realValue = "";
                            binding.inputField.setText(realValue);
                        }
                    } else if (buttonId == R.id.key_Clear) {
                        realValue = "";
                        binding.inputField.setText(realValue);
                    } else if (buttonId == R.id.key_plus || buttonId == R.id.key_minus || buttonId == R.id.key_multiple || buttonId == R.id.key_divide || buttonId == R.id.key_Mod || buttonId == R.id.key_comma) {
                        if (realValue.length() != 0 && (realValue.charAt(realValue.length() - 1) < '0' || realValue.charAt(realValue.length() - 1) > '9')) {
                            realValue = realValue.substring(0, realValue.length() - 1);
                            binding.inputField.setText(realValue);
                        }
                        if (realValue.length() != 0 || buttonId == R.id.key_minus) {
                            keyValue = ((Button) v).getText().toString();
                            binding.inputField.append(keyValue);
                        }
                        realValue = binding.inputField.getText().toString();
                        if (!checkComma(binding.inputField.getText().toString())) {
                            realValue = realValue.substring(0, realValue.length() - 1);
                            binding.inputField.setText(realValue);
                        }


                    } else if (buttonId == R.id.key_Result) {
                        // primitive solution. didn't like it.
                        binding.inputResult.setText(realValue);


                    } else if (buttonId == R.id.key_changeValue) {

                        if (realValue.length() == 0 || realValue.charAt(0) != '-') {
                            realValue = "-" + realValue;
                            binding.inputField.setText(realValue);
                        } else {
                            realValue = realValue.substring(1);
                            binding.inputField.setText(realValue);
                        }
                    } else {
                        keyValue = ((Button) v).getText().toString();
                        binding.inputField.append(keyValue);

                    }
                    realValue = binding.inputField.getText().toString();
                    result = calculatePlus(realValue);
                    realValue = String.valueOf(result);
                    binding.inputResult.setText(realValue);
                }
            });
        }
    }

    public boolean checkComma(String value) {
        String array[] = value.split("[+*/%-]");
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i] + "0";
            String array2[] = array[i].split("[,.]");
            if (array2.length > 2) {
                return false;
            }
        }
        return true;
    }

    public double calculatePlus(String value) {
        String array[] = value.split("[+]");
        double result = 0;
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i].toString());
            result += calculateMinus(array[i]);
        }
        return result;
    }

    public double calculateMinus(String value) {
        String array[] = value.split("[-]");
        if (array.length == 0) {
            return 0;
        }
        double result = calculateMultiple(array[0]);
        for (int i = 1; i < array.length; i++) {
            System.out.println(array[i].toString());
            result -= calculateMultiple(array[i]);
        }
        return result;
    }

    public double calculateMultiple(String value) {
        String array[] = value.split("[*]");
        double result = 1;
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i].toString());
            result *= calculateDivide(array[i]);
        }
        return result;
    }

    public double calculateDivide(String value) {
        String array[] = value.split("/");
        if (array.length == 0) {
            return 0;
        }
        double result = calculateMod(array[0]);
        for (int i = 1; i < array.length; i++) {
            if (calculateMod(array[i]) == 0) {
                return 1;
            }
            System.out.println(array[i].toString());
            result /= calculateMod(array[i]);
        }
        return result;
    }

    public double calculateMod(String value) {
        String array[] = value.split("%");
        if (array.length == 1 || array.length == 0) {
            if (value.length() == 0) {
                return 0;
            }
            if (array.length == 0) {
                return 0;
            }
            try {
                return Double.parseDouble(array[0]);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        double result = 0;
        try {
            result = Double.parseDouble(array[0]);

        } catch (NumberFormatException e) {

        }
        for (int i = 1; i < array.length; i++) {
            System.out.println(array[i].toString());
            result %= Double.parseDouble(array[i]);
        }
        return result;
    }


    public String deleteLast(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 'x') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

}