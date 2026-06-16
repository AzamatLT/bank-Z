
Action()
{
    char* server_response;

	lr_start_transaction("CREATE_CARD");
	
    // Регистрируем параметр для сохранения ответа (от { до })
    web_reg_save_param_ex(
        "ParamName=response_data", 
        "LB={", 
        "RB=}", 
        LAST
    );
    
	web_add_header("Content-Type", "application/json; charset=utf-8");
	    
    web_custom_request("CREATE_CARD", 
		"URL=http://localhost:48084/api/cards",
		"Method=POST", 
		"Resource=0", 
		"RecContentType=application/json", 
		"Referer=", 
		"Snapshot=t25.inf", 
		"Mode=HTML", 
		"Body={\"lastName\": \"mylastName\", \"firstName\": \"myfirstName\", \"middleName\": \"mymiddleName\", \"inn\": \"{EXINN}\", \"branchId\": 123, \"currencyId\": 2}",
        LAST);

	    // Получаем ответ сервера



    // Получаем успешность операции - ответ сервера
    server_response = lr_eval_string("{response_data}");
    
    lr_output_message("Ответ сервера: %s", server_response);

    // Проверяем наличие слова "SUCCESS"
    
    if (strstr(server_response, "SUCCESS") != NULL) 
    {
        lr_output_message("V УСПЕХ: Карта создана");
    }
    else 
    {
        lr_output_message("X ОШИБКА: %s", server_response);
    }
	
	
	lr_end_transaction("CREATE_CARD",LR_AUTO); 
	return 0;
}

