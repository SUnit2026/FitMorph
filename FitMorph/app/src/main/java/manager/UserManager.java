package manager;

import android.content.SharedPreferences;

public class UserManager {

    private SharedPreferences sp;

    public UserManager(
            SharedPreferences sp
    ){
        this.sp = sp;
    }

    public String getName(){

        return sp.getString(
                "name",
                ""
        );
    }

    public String getAge(){

        return sp.getString(
                "age",
                ""
        );
    }

    public String getGender(){

        return sp.getString(
                "gender",
                ""
        );
    }

    public String getHeight(){

        return sp.getString(
                "height",
                "170"
        );
    }

    public String getWeight(){

        return sp.getString(
                "weight",
                "70"
        );
    }

    public double getBMI(){

        try{

            double h =
                    Double.parseDouble(
                            getHeight()
                    ) / 100.0;

            double w =
                    Double.parseDouble(
                            getWeight()
                    );

            return w / (h * h);

        }catch (Exception e){

            return 0;
        }
    }

    public String getBMIState(){

        double bmi =
                getBMI();

        if(bmi < 18.5){

            return "저체중";
        }

        if(bmi < 23){

            return "정상";
        }

        if(bmi < 25){

            return "과체중";
        }

        if(bmi < 30){

            return "비만";
        }

        return "고도비만";
    }

    public int getRecommendKcal(){

        try{

            double w =
                    Double.parseDouble(
                            getWeight()
                    );

            return (int)(w * 22.5);

        }catch (Exception e){

            return 0;
        }
    }

    public String getUserInfo(){

        return
                "이름 : "
                        + getName()

                        + "\n성별 : "
                        + getGender()

                        + "\n나이 : "
                        + getAge()

                        + "\n키 : "
                        + getHeight()
                        + " cm"

                        + "\n몸무게 : "
                        + getWeight()
                        + " kg"

                        + "\nBMI : "
                        + String.format(
                        "%.2f",
                        getBMI()
                )

                        + " ("
                        + getBMIState()
                        + ")"

                        + "\n권장 칼로리 : "
                        + getRecommendKcal()
                        + " kcal";
    }

    public void updateAge(
            String age
    ){

        SharedPreferences.Editor ed =
                sp.edit();

        ed.putString(
                "age",
                age
        );

        ed.apply();
    }

    public void updateHeight(
            String height
    ){

        SharedPreferences.Editor ed =
                sp.edit();

        ed.putString(
                "height",
                height
        );

        ed.apply();
    }

    public void updateWeight(
            String weight
    ){

        SharedPreferences.Editor ed =
                sp.edit();

        ed.putString(
                "weight",
                weight
        );

        ed.apply();
    }

    public void updateName(
            String name
    ){

        SharedPreferences.Editor ed =
                sp.edit();

        ed.putString(
                "name",
                name
        );

        ed.apply();
    }

    public void updateGender(
            String gender
    ){

        SharedPreferences.Editor ed =
                sp.edit();

        ed.putString(
                "gender",
                gender
        );

        ed.apply();
    }
}