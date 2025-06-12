package z.bank.util;

import java.util.Random;

public class CardNumberGenerator {
    private static final String BIN = "4276"; // БИН вашего банка (первые 4 цифры)

    public static String generate() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder(BIN);

        // Генерация 11 цифр (BIN(4) + 11 = 15 цифр)
        for (int i = 0; i < 11; i++) {
            cardNumber.append(random.nextInt(10));
        }

        // Добавление контрольной цифры (16-я цифра) по алгоритму Луна
        int checkDigit = calculateLuhnCheckDigit(cardNumber.toString());
        cardNumber.append(checkDigit);

        return cardNumber.toString();
    }

    private static int calculateLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = false;

        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));

            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }

            sum += digit;
            alternate = !alternate;
        }

        return (10 - (sum % 10)) % 10;
    }
}
