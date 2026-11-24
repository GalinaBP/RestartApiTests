package utils;

public final class Messages {
    public static final String VALIDATION_FAILED = "Проверьте правильность заполнения полей";
    public static final String MALFORMED_REQUEST = "Некорректный запрос";
    public static final String WRONG_CREDENTIALS = "Неверная почта или пароль";
    public static final String SESSION_EXPIRED = "Сессия истекла — войдите ещё раз";
    public static final String LOGIN_REQUIRED = "Требуется вход в систему";
    public static final String EMAIL_TAKEN = "Эта почта уже зарегистрирована. Попробуйте войти";
    public static final String REGISTRATION_CLOSED = "Регистрация пока закрыта — мы откроемся совсем скоро";
    public static final String RESET_LINK_INVALID = "Ссылка не подходит или устарела. Запросите новую на экране входа";
    public static final String NOT_FOUND = "Ресурс не найден";
    public static final String METHOD_NOT_ALLOWED = "Метод не поддерживается";
    public static final String UNSUPPORTED_MEDIA_TYPE = "Неподдерживаемый формат данных";
    public static final String INTERNAL_ERROR = "Произошла непредвиденная ошибка";

    public static final String EMAIL_REQUIRED = "Укажите электронную почту";
    public static final String EMAIL_INVALID = "Похоже, в адресе почты опечатка";
    public static final String EMAIL_TOO_LONG = "Слишком длинный адрес почты";
    public static final String PASSWORD_REQUIRED_ON_REGISTER = "Придумайте пароль";
    public static final String PASSWORD_LENGTH = "Пароль должен быть от 8 до 72 символов";
    public static final String PASSWORD_REQUIRED_ON_LOGIN = "Введите пароль";
    public static final String REFRESH_TOKEN_REQUIRED = "Не передан токен обновления";
    public static final String RESET_TOKEN_REQUIRED = "В ссылке не хватает кода — откройте её из письма ещё раз";

    private Messages() {
    }
}
