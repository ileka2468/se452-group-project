package edu.depaul.cdm.se452.rfa.profileManagement.service;
import java.util.HashMap;
import java.util.Map;

import static edu.depaul.cdm.se452.rfa.profileManagement.service.CharacteristicType.scalar;
import static edu.depaul.cdm.se452.rfa.profileManagement.service.CharacteristicType.binary;
import static edu.depaul.cdm.se452.rfa.profileManagement.service.CharacteristicType.categorical;


public class Characteristics {
    private String genderPreference;
    private boolean smokingPreference;
    private boolean alcoholUsage;
    private int cleanlinessLevel; // 1-5 scale
    private int noiseTolerance;   // 1-5 scale
    private int hygiene;          // 1-5 scale
    private String sleepSchedule;
    private int guestsVisitors;   // 1-5 scale
    private int workStudyFromHome; // 1-5 scale
    private int petTolerance;     // 1-5 scale
    private int sharedExpenses;   // 1-5 scale
    private int studyHabits;      // 1-5 scale
    private int roomPrivacy;      // 1-5 scale
    private int cookingFrequency; // 1-5 scale
    private boolean foodSharing;
    private int exerciseFrequency; // 1-5 scale
    private String personalityType;
    private int sharedLivingSpaceUse; // 1-5 scale
    private int roomTemperaturePreference; // 1-5 scale
    private String decoratingStyle;

    public Characteristics() {
        this("No Preference", false, false, 3, 3, 3, "Flexible", 3, 3, 3, 3, 3, 3, 3, false, 3, "Ambivert", 3, 3, "Modern");
    }


    public Characteristics(String genderPreference, boolean smokingPreference, boolean alcoholUsage, int cleanlinessLevel,
                               int noiseTolerance, int hygiene, String sleepSchedule, int guestsVisitors, int workStudyFromHome,
                               int petTolerance, int sharedExpenses, int studyHabits, int roomPrivacy, int cookingFrequency,
                               boolean foodSharing, int exerciseFrequency, String personalityType, int sharedLivingSpaceUse,
                               int roomTemperaturePreference, String decoratingStyle) {
        this.genderPreference = genderPreference;
        this.smokingPreference = smokingPreference;
        this.alcoholUsage = alcoholUsage;
        this.cleanlinessLevel = cleanlinessLevel;
        this.noiseTolerance = noiseTolerance;
        this.hygiene = hygiene;
        this.sleepSchedule = sleepSchedule;
        this.guestsVisitors = guestsVisitors;
        this.workStudyFromHome = workStudyFromHome;
        this.petTolerance = petTolerance;
        this.sharedExpenses = sharedExpenses;
        this.studyHabits = studyHabits;
        this.roomPrivacy = roomPrivacy;
        this.cookingFrequency = cookingFrequency;
        this.foodSharing = foodSharing;
        this.exerciseFrequency = exerciseFrequency;
        this.personalityType = personalityType;
        this.sharedLivingSpaceUse = sharedLivingSpaceUse;
        this.roomTemperaturePreference = roomTemperaturePreference;
        this.decoratingStyle = decoratingStyle;
        ValidateCharacteristics();
    }



    public Map<String, Object> toMap() {
        Map<String, Object> preferencesMap = new HashMap<>();
        preferencesMap.put("gender_preference", genderPreference);
        preferencesMap.put("smoking_preference", smokingPreference);
        preferencesMap.put("alcohol_usage", alcoholUsage);
        preferencesMap.put("cleanliness_level", cleanlinessLevel);
        preferencesMap.put("noise_tolerance", noiseTolerance);
        preferencesMap.put("hygiene", hygiene);
        preferencesMap.put("sleep_schedule", sleepSchedule);
        preferencesMap.put("guests_visitors", guestsVisitors);
        preferencesMap.put("work_study_from_home", workStudyFromHome);
        preferencesMap.put("pet_tolerance", petTolerance);
        preferencesMap.put("shared_expenses", sharedExpenses);
        preferencesMap.put("study_habits", studyHabits);
        preferencesMap.put("room_privacy", roomPrivacy);
        preferencesMap.put("cooking_frequency", cookingFrequency);
        preferencesMap.put("food_sharing", foodSharing);
        preferencesMap.put("exercise_frequency", exerciseFrequency);
        preferencesMap.put("personality_type", personalityType);
        preferencesMap.put("shared_living_space_use", sharedLivingSpaceUse);
        preferencesMap.put("room_temperature_preference", roomTemperaturePreference);
        preferencesMap.put("decorating_style", decoratingStyle);
        return preferencesMap;
    }


    @Override
    public String toString() {
        return "RoommatePreferences{" +
                "genderPreference='" + genderPreference + '\'' +
                ", smokingPreference=" + smokingPreference +
                ", alcoholUsage=" + alcoholUsage +
                ", cleanlinessLevel=" + cleanlinessLevel +
                ", noiseTolerance=" + noiseTolerance +
                ", hygiene=" + hygiene +
                ", sleepSchedule='" + sleepSchedule + '\'' +
                ", guestsVisitors=" + guestsVisitors +
                ", workStudyFromHome=" + workStudyFromHome +
                ", petTolerance=" + petTolerance +
                ", sharedExpenses=" + sharedExpenses +
                ", studyHabits=" + studyHabits +
                ", roomPrivacy=" + roomPrivacy +
                ", cookingFrequency=" + cookingFrequency +
                ", foodSharing=" + foodSharing +
                ", exerciseFrequency=" + exerciseFrequency +
                ", personalityType='" + personalityType + '\'' +
                ", sharedLivingSpaceUse=" + sharedLivingSpaceUse +
                ", roomTemperaturePreference=" + roomTemperaturePreference +
                ", decoratingStyle='" + decoratingStyle + '\'' +
                '}';
    }


    public static void main(String[] args) {
        Characteristics preferences = new Characteristics(
                "No Preference", false, false, 5, 4, 5, "Night Owl", 3, 2, 5, 4, 3, 4, 2, false, 4, "Ambivert", 3, 3, "Modern");

        preferences.getSleepSchedule();
        System.out.println(preferences.toString());

        Map<String, Object> preferencesMap = preferences.toMap();
        System.out.println(preferencesMap);
    }


    public CharacteristicDataWithOptions<String> getGenderPreference() {
        return new CharacteristicDataWithOptions<>(genderPreference, categorical, new String[]{"No Preference", "Male", "Female", "Non-Binary"});
    }

    public void setGenderPreference(String genderPreference) {
        this.genderPreference = genderPreference;
    }

    public CharacteristicData<Boolean> getSmokingPreference() {
        return new CharacteristicData<>(smokingPreference, binary);
    }

    public void setSmokingPreference(boolean smokingPreference) {
        this.smokingPreference = smokingPreference;
    }

    public CharacteristicData<Boolean> getAlcoholUsage() {
        return new CharacteristicData<>(alcoholUsage, binary);
    }

    public void setAlcoholUsage(boolean alcoholUsage) {
        this.alcoholUsage = alcoholUsage;
    }

    public CharacteristicData<Integer> getCleanlinessLevel() {
        return new CharacteristicData<>(cleanlinessLevel, scalar);
    }

    public void setCleanlinessLevel(int cleanlinessLevel) {
        this.cleanlinessLevel = cleanlinessLevel;
    }

    public CharacteristicData<Integer> getNoiseTolerance() {
        return new CharacteristicData<>(noiseTolerance, scalar);
    }

    public void setNoiseTolerance(int noiseTolerance) {
        this.noiseTolerance = noiseTolerance;
    }

    public CharacteristicData<Integer> getHygiene() {
        return new CharacteristicData<>(hygiene, scalar);
    }

    public void setHygiene(int hygiene) {
        this.hygiene = hygiene;
    }

    public CharacteristicDataWithOptions<String> getSleepSchedule() {
        return new CharacteristicDataWithOptions<>(sleepSchedule, categorical, new String[]{"Early Riser", "Night Owl", "Flexible"});
    }

    public void setSleepSchedule(String sleepSchedule) {
        this.sleepSchedule = sleepSchedule;
    }

    public CharacteristicData<Integer> getGuestsVisitors() {
        return new CharacteristicData<>(guestsVisitors, scalar);
    }

    public void setGuestsVisitors(int guestsVisitors) {
        this.guestsVisitors = guestsVisitors;
    }

    public CharacteristicData<Integer> getWorkStudyFromHome() {
        return new CharacteristicData<>(workStudyFromHome, scalar);
    }

    public void setWorkStudyFromHome(int workStudyFromHome) {
        this.workStudyFromHome = workStudyFromHome;
    }

    public CharacteristicData<Integer> getPetTolerance() {
        return new CharacteristicData<>(petTolerance, scalar);
    }

    public void setPetTolerance(int petTolerance) {
        this.petTolerance = petTolerance;
    }

    public CharacteristicData<Integer> getSharedExpenses() {
        return new CharacteristicData<>(sharedExpenses, scalar);
    }

    public void setSharedExpenses(int sharedExpenses) {
        this.sharedExpenses = sharedExpenses;
    }

    public CharacteristicData<Integer> getStudyHabits() {
        return new CharacteristicData<>(studyHabits, scalar);
    }

    public void setStudyHabits(int studyHabits) {
        this.studyHabits = studyHabits;
    }

    public CharacteristicData<Integer> getRoomPrivacy() {
        return new CharacteristicData<>(roomPrivacy, scalar);
    }

    public void setRoomPrivacy(int roomPrivacy) {
        this.roomPrivacy = roomPrivacy;
    }

    public CharacteristicData<Integer> getCookingFrequency() {
        return new CharacteristicData<>(cookingFrequency, scalar);
    }

    public void setCookingFrequency(int cookingFrequency) {
        this.cookingFrequency = cookingFrequency;
    }

    public CharacteristicData<Boolean> getFoodSharing() {
        return new CharacteristicData<>(foodSharing, binary);
    }

    public void setFoodSharing(boolean foodSharing) {
        this.foodSharing = foodSharing;
    }

    public CharacteristicData<Integer> getExerciseFrequency() {
        return new CharacteristicData<>(exerciseFrequency, scalar);
    }

    public void setExerciseFrequency(int exerciseFrequency) {
        this.exerciseFrequency = exerciseFrequency;
    }

    public CharacteristicDataWithOptions<String> getPersonalityType() {
        return new CharacteristicDataWithOptions<>(personalityType, categorical, new String[]{"Introverted", "Extroverted", "Ambivert"});
    }

    public void setPersonalityType(String personalityType) {
        this.personalityType = personalityType;
    }

    public CharacteristicData<Integer> getSharedLivingSpaceUse() {
        return new CharacteristicData<>(sharedLivingSpaceUse, scalar);
    }

    public void setSharedLivingSpaceUse(int sharedLivingSpaceUse) {
        this.sharedLivingSpaceUse = sharedLivingSpaceUse;
    }

    public CharacteristicData<Integer> getRoomTemperaturePreference() {
        return new CharacteristicData<>(roomTemperaturePreference, scalar);
    }

    public void setRoomTemperaturePreference(int roomTemperaturePreference) {
        this.roomTemperaturePreference = roomTemperaturePreference;
    }

    public CharacteristicDataWithOptions<String> getDecoratingStyle() {
        return new CharacteristicDataWithOptions<>(decoratingStyle, categorical, new String[]{"Minimalist", "Cozy", "Modern", "Traditional"});
    }

    public void setDecoratingStyle(String decoratingStyle) {
        this.decoratingStyle = decoratingStyle;
    }

    public void ValidateCharacteristics() throws InvalidCharacteristicException{

            // call all getters
            getGenderPreference();
            getSmokingPreference();
            getAlcoholUsage();
            getCleanlinessLevel();
            getNoiseTolerance();
            getHygiene();
            getSleepSchedule();
            getGuestsVisitors();
            getWorkStudyFromHome();
            getPetTolerance();
            getSharedExpenses();
            getStudyHabits();
            getRoomPrivacy();
            getCookingFrequency();
            getFoodSharing();
            getExerciseFrequency();
            getPersonalityType();
            getSharedLivingSpaceUse();
            getRoomTemperaturePreference();
            getDecoratingStyle();
    }

}
