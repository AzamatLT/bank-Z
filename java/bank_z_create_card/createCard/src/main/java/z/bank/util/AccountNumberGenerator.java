package z.bank.util;

import java.util.Random;

public class AccountNumberGenerator {
    private static final String DEFAULT_BALANCE_ACCOUNT = "40817"; // 40817 - счёт физ.лица

    public static String generate(String branchId, String currencyCode, String clientInn) {
        // 1. Балансовый счёт (5 цифр) - 40817 для физ.лица
        String balanceAccount = DEFAULT_BALANCE_ACCOUNT;

        // 2. Код валюты (3 цифры)
        String currency = String.format("%03d", Integer.parseInt(currencyCode));

        // 3. Код филиала (4 цифры)
        String branchCode = String.format("%04d", Integer.parseInt(branchId));

        // 4. Уникальная часть (8 цифр) - комбинация INN и случайных чисел
        String uniquePart = generateUniquePart(clientInn);

        // Собираем полный номер (5 + 3 + 4 + 8 = 20 цифр)
        return balanceAccount + currency + branchCode + uniquePart;
    }

    private static String generateUniquePart(String inn) {
        // Используем часть INN (первые 5 цифр) + случайные числа
        String innPart = inn.length() > 5 ? inn.substring(0, 5) :
                String.format("%5s", inn).replace(' ', '0');

        Random random = new Random();
        String randomPart = String.format("%03d", random.nextInt(1000));

        return innPart + randomPart;
    }
}

//Структура номера счета (20 цифр):
//Балансовый счет (5 цифр):
//40817 - для счетов физических лиц
//40820 - для карточных счетов
//Код валюты (3 цифры):
//810 - российский рубль
//840 - доллар США
//978 - евро
//Код филиала/отделения (4 цифры)
//Уникальная часть (8 цифр):
//Первые 5 цифр - часть ИНН клиента
//Последние 3 цифр - случайное число
