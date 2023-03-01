public class Test {
    private String millisToDuration(long dur){
        long millis = dur;
        long hours = millis / (60 * 60 * 1000);  // convert to hours
        millis %= (60 * 60 * 1000);
        long minutes = millis / (60 * 1000); // convert to minutes
        millis %= (60 * 1000);
        long seconds = millis / 1000; // convert to seconds
        return (hours/10 > 1? hours: "0"+hours) + ":" +
                (minutes/10 > 0? minutes: "0"+minutes) + ":" +
                (seconds/10 > 0? seconds: "0"+seconds);
    }

    private long durationToMillis(String dur){
        String[] durs = dur.split(":");
        var hrs = Long.parseLong(durs[0]) * 60 * 60;
        var min = Long.parseLong(durs[1]) * 60;
        var sec = Long.parseLong(durs[2]);
        return (hrs + min + sec) * 1000;
    }

    public static void main(String[] args) {
        var a = new Test().durationToMillis("00:05:07");
        var b = new Test().millisToDuration(307000);
        System.out.println(a);
        System.out.println(b);
    }
}
