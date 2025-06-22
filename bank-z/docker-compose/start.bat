@echo off
chcp 65001 > nul
cls

REM Включение поддержки ANSI-цветов
reg add HKCU\Console /v VirtualTerminalLevel /t REG_DWORD /d 1 /f > nul 2>&1

REM Настройки цветов
set GREEN=[92m
set YELLOW=[93m
set RED=[91m
set BLUE=[94m
set MAGENTA=[95m
set CYAN=[96m
set RESET=[0m
set BOLD=[1m

echo %BOLD%%BLUE%=== Запуск системы Bank-Z ===%RESET%
echo.

REM Проверка доступности Docker
docker version >nul 2>&1
if %errorlevel% neq 0 (
    echo %RED%Ошибка: Docker не запущен или не настроен%RESET%
    echo %YELLOW%Проверьте:
    echo 1. Запущен ли Docker Desktop
    echo 2. Настроены ли Shared Drives
    echo 3. Включен ли WSL2%RESET%
    pause
    exit /b 1
)

REM 1. Проверка и создание Docker сетей
echo %YELLOW%[1/5] Проверка Docker сетей...%RESET%
for %%N in (bank_z) do (
    call :check_network %%N
)
echo.

REM 2. Запуск monitoring-stack
echo %YELLOW%[2/5] Запуск monitoring-stack...%RESET%
call :exec_command "docker-compose -f ./monitoring-stack/docker-compose.monitoring.yml up -d" "Monitoring Stack"
call :check_containers "./monitoring-stack/docker-compose.monitoring.yml"
echo.

REM 3. Запуск postgres-stack
echo %YELLOW%[3/5] Запуск postgres-stack...%RESET%
call :exec_command "docker-compose -f ./postgres-stack/docker-compose.postgres.yml up -d" "PostgreSQL Stack"
call :check_containers "./postgres-stack/docker-compose.postgres.yml"
echo.

REM 4. Запуск kafka-stack
echo %YELLOW%[4/5] Запуск kafka-stack...%RESET%
call :exec_command "docker-compose -f ./kafka-stack/docker-compose.kafka.yml up -d" "Kafka Stack"
call :check_containers "./kafka-stack/docker-compose.kafka.yml"
echo.

REM 5. Запуск сервисов
echo %YELLOW%[5/5] Запуск сервисов...%RESET%
call :exec_command "docker-compose -f ./services/docker-compose.services_create_card.yml up -d" "Create Card Service"
call :check_containers "./services/docker-compose.services_create_card.yml"
echo.

REM Финальная проверка
echo %YELLOW%Проверка всех контейнеров:%RESET%
docker ps -a --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
echo.

echo %BOLD%%GREEN%=== Все компоненты успешно запущены! ===%RESET%
echo %CYAN%Команды управления:%RESET%
echo - Просмотр логов: docker-compose -f [путь] logs -f
echo - Остановка системы: stop.bat
echo - Перезагрузка: restart.bat
echo.
pause
exit /b

REM ================================================
REM Функция проверки наличия Docker сети
:check_network
docker network inspect %1 >nul 2>&1
if %errorlevel% equ 0 (
    echo %GREEN%✓ Сеть %1 уже существует%RESET%
) else (
    call :exec_command "docker network create %1" "Создание сети %1"
)
exit /b

REM ================================================
REM Функция выполнения команд с обработкой ошибок
:exec_command
echo %MAGENTA%> %~1%RESET%
%~1
if %errorlevel% neq 0 (
    echo %RED%✗ Ошибка: %~2%RESET%
    echo %YELLOW%Логи ошибки:%RESET%
    
    echo %~1 | findstr /i "docker-compose" >nul
    if %errorlevel% equ 0 (
        docker-compose logs --tail=20 2>&1
    )
    
    echo %RED%Исправьте ошибку и перезапустите систему%RESET%
    pause
    exit /b %errorlevel%
) else (
    echo %GREEN%✓ Успешно: %~2%RESET%
)
exit /b

REM ================================================
REM Функция проверки состояния контейнеров
:check_containers
echo %CYAN%Проверка состояния (%1):%RESET%
docker-compose -f %1 ps --services | findstr /v "Name" > containers.tmp

for /F "tokens=*" %%S in (containers.tmp) do (
    for /F "tokens=1,4" %%A in ('docker-compose -f %1 ps --filter "status=running" --format "{{.Name}} {{.Status}}" ^| findstr "%%S"') do (
        if "%%B"=="" (
            echo %RED%✗ Сервис %%A не запущен%RESET%
        ) else (
            echo %GREEN%✓ %%A: %%B%RESET%
        )
    )
)
del containers.tmp >nul 2>&1
exit /b