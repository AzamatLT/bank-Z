Action()
{
    char* server_response;


    lr_start_transaction("CREATE_CLIENTS");
    
    // Регистрируем параметр для сохранения ответа (от { до })
    web_reg_save_param_ex(
        "ParamName=response_data", 
        "LB={", 
        "RB=}", 
        LAST
    );
    
    // Отправляем POST-запрос
    web_add_header("Content-Type", "application/json; charset=utf-8");
    
    web_custom_request("CREATE_CLIENTS", 
        "URL=http://localhost:48085/api/clients",
        "Method=POST", 
        "Resource=0", 
        "RecContentType=application/json", 
        "Referer=", 
        "Snapshot=t25.inf", 
        "Mode=HTML", 
        "Body={\"lastName\": \"mylastName\", \"firstName\": \"myfirstName\", \"middleName\": \"mymiddleName\", \"inn\": \"{NEWINN}\", \"branchCode\": \"123\"}",
        LAST
    );

    // Получаем ответ сервера
    server_response = lr_eval_string("{response_data}");

    // Проверяем успешность операции - наличие слова "success"
    if (strstr(server_response, "success") != NULL) 
    {
        lr_output_message("V УСПЕХ: Клиен создан");
    }
    else 
    {
        lr_output_message("X ОШИБКА: %s", server_response);
    }
    
    lr_end_transaction("CREATE_CLIENTS", LR_AUTO); 
    
    return 0;
}