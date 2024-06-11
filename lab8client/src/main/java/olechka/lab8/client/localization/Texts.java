package olechka.lab8.client.localization;

import lombok.Getter;
import olechka.lab8.exceptions.CommandFailureType;

import java.util.Locale;
import java.util.Map;

@Getter
public class Texts {
    private String loginWindowTitle;
    private String errorWindowTitle;
    private String stateWindowTitle;
    private String tableWindowTitle;
    private String elementWindowTitle;
    private Object[] tableTitles;
    private String menuAddElement;
    private String menuClearItem;
    private String openTableItem;
    private String elementMenu;
    private String menuHelpItem;
    private String logOutItem;
    private String descriptionHelp;
    private Map<CommandFailureType, String> errorTypes;

    private Texts() {
    }

    public static Texts getCurrent() {
//        класс который определяет код языка + код страны.
        Locale locale = Locale.getDefault();
        if (locale.getLanguage().equals("sk")) {
            return getSlavic();
        } else if (locale.getLanguage().equals("pl")) {
            return getPolish();
        } else if (locale.getLanguage().equals("en") && locale.getCountry().equals("AU")) {
            return getEnglishAU();
        } else {
            return getRussian();
        }

    }

    public static Texts getRussian() {
        Texts texts = new Texts();
        texts.loginWindowTitle = "Вход";
        texts.errorWindowTitle = "Ошибка";
        texts.stateWindowTitle = "Учебные группы";
        texts.tableWindowTitle = "Учебные группы (таблица)";
        texts.elementWindowTitle = "Информация об учебной группе";
        texts.tableTitles = new Object[]{"ID", "Имя", "Дата создания", "Координаты", "Студенты", "Переведенные студенты", "Семестр", "Форма обучения", "Админ"};
        texts.menuAddElement = "Добавить новый...";
        texts.menuClearItem = "Удалить мои элементы";
        texts.openTableItem = "Открыть таблицу";
        texts.elementMenu = "Элементы";
        texts.menuHelpItem = "Помощь";
        texts.logOutItem = "Выйти из аккаунта";
        texts.descriptionHelp = "";
        texts.errorTypes = Map.ofEntries(Map.entry(CommandFailureType.SAME_KEY, "Вы пытаетесь добавить groupAdmin с существующим passportId"),
                Map.entry(CommandFailureType.NO_COMPARISON_CANDIDATE, "К сожалению, нет элементов, с которым можно сравнить данный"),
                Map.entry(CommandFailureType.COMPARISON_CONSTRAINT_FAILED, "Требуемое условие не выполнено"),
                Map.entry(CommandFailureType.NO_OWN_ELEMENTS, "У вас нет элементов"),
                Map.entry(CommandFailureType.NO_ELEMENT_FOUND, "Элементов, подходящих под условие, не найдено"),
                Map.entry(CommandFailureType.NO_RIGHTS, "У вас нет прав управлять данным объектом"),
                Map.entry(CommandFailureType.USER_LOGIN_FAILED, "Неверный логин или пароль"),
                Map.entry(CommandFailureType.USER_REGISTER_FAILED, "Невозможно зарегистрироваться под этим логином"));
        return texts;
    }

    public static Texts getSlavic() {
        Texts texts = new Texts();
        texts.loginWindowTitle = "Prihlásenie";
        texts.errorWindowTitle = "Chyba";
        texts.stateWindowTitle = "Študijné skupiny";
        texts.tableWindowTitle = "Študijné skupiny (tabuľka)";
        texts.elementWindowTitle = "Informácie o študijnej skupine";
        texts.tableTitles = new Object[]{"ID", "Meno", "Dátum vytvorenia", "Súradnice", "Študenti", "Preložení študenti", "Semester", "Forma štúdia", "Admin"};
        texts.menuAddElement = "Pridať nový...";
        texts.menuClearItem = "Odstrániť moje položky";
        texts.openTableItem = "Otvoriť tabuľku";
        texts.elementMenu = "Položky";
        texts.menuHelpItem = "Otvohdiť";
        texts.logOutItem = "Odstrániť moje";
        texts.descriptionHelp = "";
        texts.errorTypes = Map.ofEntries(
                Map.entry(CommandFailureType.SAME_KEY, "Pokúšate sa pridať groupAdmin s existujúcim passportId"),
                Map.entry(CommandFailureType.NO_COMPARISON_CANDIDATE, "Bohužiaľ, nie sú k dispozícii žiadne prvky na porovnanie"),
                Map.entry(CommandFailureType.COMPARISON_CONSTRAINT_FAILED, "Požadovaná podmienka nie je splnená"),
                Map.entry(CommandFailureType.NO_OWN_ELEMENTS, "Nemáte žiadne prvky"),
                Map.entry(CommandFailureType.NO_ELEMENT_FOUND, "Neboli nájdené žiadne prvky zodpovedajúce podmienke"),
                Map.entry(CommandFailureType.NO_RIGHTS, "Nemáte práva na správu tohto objektu"),
                Map.entry(CommandFailureType.USER_LOGIN_FAILED, "Nesprávne prihlasovacie meno alebo heslo"),
                Map.entry(CommandFailureType.USER_REGISTER_FAILED, "Nie je možné sa zaregistrovať s týmto menom")
        );
        return texts;
    }

    public static Texts getPolish() {
        Texts texts = new Texts();
        texts.loginWindowTitle = "Logowanie";
        texts.errorWindowTitle = "Błąd";
        texts.stateWindowTitle = "Grupy studenckie";
        texts.tableWindowTitle = "Grupy studenckie (tabela)";
        texts.elementWindowTitle = "Informacje o grupie studenckiej";
        texts.tableTitles = new Object[]{"ID", "Nazwa", "Data utworzenia", "Współrzędne", "Studenci", "Przeniesieni studenci", "Semestr", "Forma nauki", "Administrator"};
        texts.menuAddElement = "Dodaj nowy...";
        texts.menuClearItem = "Usuń moje pozycje";
        texts.openTableItem = "Otwórz tabelę";
        texts.elementMenu = "Pozycje";
        texts.menuHelpItem = "Nazwa";
        texts.logOutItem = "Dodaj nowy";
        texts.descriptionHelp = "";
        texts.errorTypes = Map.ofEntries(
                Map.entry(CommandFailureType.SAME_KEY, "Próbujesz dodać groupAdmin z istniejącym passportId"),
                Map.entry(CommandFailureType.NO_COMPARISON_CANDIDATE, "Niestety, brak elementów do porównania"),
                Map.entry(CommandFailureType.COMPARISON_CONSTRAINT_FAILED, "Wymagany warunek nie jest spełniony"),
                Map.entry(CommandFailureType.NO_OWN_ELEMENTS, "Nie masz żadnych elementów"),
                Map.entry(CommandFailureType.NO_ELEMENT_FOUND, "Nie znaleziono elementów spełniających warunek"),
                Map.entry(CommandFailureType.NO_RIGHTS, "Nie masz uprawnień do zarządzania tym obiektem"),
                Map.entry(CommandFailureType.USER_LOGIN_FAILED, "Niepoprawny login lub hasło"),
                Map.entry(CommandFailureType.USER_REGISTER_FAILED, "Nie można zarejestrować się z tym loginem")
        );
        return texts;
    }

    public static Texts getEnglishAU() {
        Texts texts = new Texts();
        texts.loginWindowTitle = "Login";
        texts.errorWindowTitle = "Error";
        texts.stateWindowTitle = "Study Groups";
        texts.tableWindowTitle = "Study Groups (Table)";
        texts.elementWindowTitle = "Study Group Information";
        texts.tableTitles = new Object[]{"ID", "Name", "Creation Date", "Coordinates", "Students", "Transferred Students", "Semester", "Mode of Study", "Admin"};
        texts.menuAddElement = "Add New...";
        texts.menuClearItem = "Remove My Items";
        texts.openTableItem = "Open Table";
        texts.elementMenu = "Items";
        texts.menuHelpItem = "Help";
        texts.logOutItem = "Log out";
        texts.descriptionHelp = "";
        texts.errorTypes = Map.ofEntries(
                Map.entry(CommandFailureType.SAME_KEY, "You are trying to add groupAdmin with an existing passportId"),
                Map.entry(CommandFailureType.NO_COMPARISON_CANDIDATE, "Unfortunately, there are no items to compare with"),
                Map.entry(CommandFailureType.COMPARISON_CONSTRAINT_FAILED, "Required condition not met"),
                Map.entry(CommandFailureType.NO_OWN_ELEMENTS, "You have no items"),
                Map.entry(CommandFailureType.NO_ELEMENT_FOUND, "No items matching the condition found"),
                Map.entry(CommandFailureType.NO_RIGHTS, "You do not have the rights to manage this object"),
                Map.entry(CommandFailureType.USER_LOGIN_FAILED, "Incorrect login or password"),
                Map.entry(CommandFailureType.USER_REGISTER_FAILED, "Unable to register with this login")
        );
        return texts;
    }
}
