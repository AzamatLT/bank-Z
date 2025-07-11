

# Проверяем Ansible
ansible --version

# Проверяем JMeter
jmeter --version

# Пример использования JMeter

# Запуск теста в командной строке
jmeter -n -t test_plan.jmx -l result.jtl

# Генерация отчёта
jmeter -g result.jtl -o report