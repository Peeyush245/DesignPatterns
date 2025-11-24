import Builder.User;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        User user = User.builder()
                .setAge(29)
                .setName("Peeyush")
                .setEmail("peeyushpandey245@gmail.com")
                .build();

        System.out.println(user.toString());
    }
}