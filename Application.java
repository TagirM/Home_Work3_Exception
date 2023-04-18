import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

// Напишите приложение, которое будет запрашивать у пользователя следующие данные в произвольном порядке, 
// разделенные пробелом:
// Фамилия Имя Отчество дата рождения номер телефона пол
// Форматы данных:
// фамилия, имя, отчество - строки
// дата_рождения - строка формата dd.mm.yyyy
// номер_телефона - целое беззнаковое число без форматирования
// пол - символ латиницей f или m.

// Приложение должно проверить введенные данные по количеству. Если количество не совпадает с требуемым, 
// вернуть код ошибки, обработать его и показать пользователю сообщение, что он ввел меньше и больше данных, 
// чем требуется.
// Приложение должно попытаться распарсить полученные значения и выделить из них требуемые параметры. 
// Если форматы данных не совпадают, нужно бросить исключение, соответствующее типу проблемы. Можно 
// использовать встроенные типы java и создать свои. Исключение должно быть корректно обработано, 
// пользователю выведено сообщение с информацией, что именно неверно.
// Если всё введено и обработано верно, должен создаться файл с названием, равным фамилии, в него в одну 
// строку должны записаться полученные данные, вида

// <Фамилия><Имя><Отчество><датарождения> <номертелефона><пол>

// Однофамильцы должны записаться в один и тот же файл, в отдельные строки.
// Не забудьте закрыть соединение с файлом.
// При возникновении проблемы с чтением-записью в файл, исключение должно быть корректно обработано, пользователь 
// должен увидеть стектрейс ошибки.

public class Application {

    public static String[] data(String[] dataMan) throws Exception {
        System.out.println("Enter data in random order separated by a space (in english):");
        System.out.println("Last name First name Middle name, date of birth, phone number, gender");
        Scanner scan = new Scanner(System.in);
        String text = scan.nextLine();
        scan.close();
        String result = "";

        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            result += text.charAt(i);
            try {
                if (text.charAt(i) == ' ') {
                    result = result.replace(" ", "");
                    dataMan[j] = result;
                    result = "";
                    j++;
                }
                if (i == text.length() - 1) {
                    if (j < 5) {
                        throw new ElementNeed();
                    }
                    result = result.replace(" ", "");
                    dataMan[j] = result;
                }
            } catch (IndexOutOfBoundsException e) {
                throw new ElementAbsent();
            }
        }
        return dataMan;
    }

    public static void main(String[] args) throws Exception {
        String[] dataMan = new String[6];
        String lastName = "";
        String fileName = "";
        System.out.println(Arrays.toString(data(dataMan)));
        int count = 0;
        for (String elem : dataMan) {
            char firstChar = elem.charAt(0);
            if (elem.length() == 1) {
                if (firstChar != 102 && firstChar != 109) {
                    throw new GenderInvalid();
                }
            } else if (firstChar >= 48 && firstChar < 52) {
                try {
                    Calendar calendar = new GregorianCalendar();
                    SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                    calendar.setLenient(false);
                    calendar.setTime(df.parse(elem));
                } catch (ParseException e) {
                    throw new DateInvalid();
                }
            } else if (firstChar >= 55 && firstChar < 57 && elem.length() > 10) {
                if (elem.length() == 11) {
                    try {
                        long tel = Long.parseLong(elem);
                    } catch (NumberFormatException e) {
                        throw new TelephoneInvalid();
                    }
                } else {
                    throw new TelephoneInvalid();
                }
            } else if (firstChar > 64 && firstChar < 91) {
                if (count == 0) {
                    lastName = elem;                    
                }
                count++;
                for (int i = 1; i < elem.length(); i++) {
                    char word = elem.charAt(i);
                    if (word < 97 || word > 122) {
                        throw new NameInvalid();
                    }
                }               
            } else {
                throw new DataInvalid();
            }
        }
        fileName = lastName + ".txt";        
        try (FileWriter writer = new FileWriter(fileName, true)) {
            for (String i : dataMan) {                    
                    writer.append("<");
                    writer.append(i);
                    writer.append(">");
                }
            writer.append('\n');
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("Successfully");
    }
}
