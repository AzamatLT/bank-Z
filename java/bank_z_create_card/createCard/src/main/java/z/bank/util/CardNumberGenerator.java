package z.bank.util;

import java.util.Random;

public class CardNumberGenerator {
    private static final String BIN = "4276"; // БИН банка
    private static final Random random = new Random();

    public static String generate() {
        StringBuilder cardNumber = new StringBuilder(BIN);
        System.out.println("cardNumber_1_______________________________" + cardNumber.toString());
        // Генерация 12 цифр
        for (int i = 0; i < 12; i++) {
            cardNumber.append(random.nextInt(10));
        }
        System.out.println("cardNumber_2_______________________________" + cardNumber.toString());
        // Добавление контрольной цифры (упрощенный алгоритм)
        int sum = 0;
        for (int i = 0; i < cardNumber.length(); i++) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            if (i % 2 == 0) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }
            sum += digit;
        }
        int checkDigit = (10 - (sum % 10)) % 10;
        cardNumber.append(checkDigit);

        System.out.println("cardNumber_3_______________________________" + cardNumber.toString());

        return cardNumber.toString();
    }
}