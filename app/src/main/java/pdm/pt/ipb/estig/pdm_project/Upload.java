package pdm.pt.ipb.estig.pdm_project;

/**
 * Class Upload - This class is a classs to create objects uploads.
 *
 * @author Ricardo Lucas - nº 15297
 * @version 23/09/2018
 */

import com.google.firebase.database.Exclude;

public class Upload {
    private String mTitle;
    private String mDescription;
    private String mCity;
    private String mCountry;
    private String mLatitude;
    private String mLongitude;
    private String mCreatorId;
    private String mImageUrl;
    private String mKey;
    private String mDate;

    public Upload(){
        //empty constructor needed
    }

    /**
     * Method Upload - Constructor.
     * @param title - title.
     * @param description - description.
     * @param city - city.
     * @param country - country.
     * @param latitude - latitude.
     * @param longitude - longitude.
     * @param creatorId - Creator unique Id.
     * @param date - date.
     * @param imageUrl - image download link.
     *
     */
    public Upload(String title, String description, String city, String country, String latitude, String longitude, String creatorId, String date, String imageUrl){
        if(title.trim().equals("")){

            title = "No name";

        }

        mTitle = title;

        mDescription = description;

        mCity = city;

        mCountry = country;

        mLatitude = latitude;

        mLongitude = longitude;

        mCreatorId = creatorId;

        mImageUrl = imageUrl;

        mDate = date;

    }

    public String getTitle(){
        return mTitle;
    }

    /**
     * Method setTitle - Set's a title.
     * @param title - title.
     *
     */
    public void setTitle(String title){
        mTitle = title;
    }

    /**
     * Method getDescription - Get's description.
     * @return mDescription.
     *
     */
    public String getDescription(){
        return mDescription;
    }

    /**
     * Method setDescription - Set's a description.
     * @param description - description.
     *
     */
    public void setDescription(String description){
        mDescription = description;
    }

    /**
     * Method getCity - Get's city.
     * @return mCity.
     *
     */
    public String getCity(){
        return mCity;
    }

    /**
     * Method setCity - Set's a city.
     * @param city - city.
     *
     */
    public void setCity(String city){
        mCity = city;
    }

    /**
     * Method getCountry - Get's country.
     * @return mCountry.
     *
     */
    public String getCountry(){
        return mCountry;
    }

    /**
     * Method setCountry - Set's a country.
     * @param country - country.
     *
     */
    public void setCountry(String country){
        mCountry = country;
    }

    /**
     * Method getLatitude - Get's latitude.
     * @return mLatitude.
     *
     */
    public String getLatitude(){
        return mLatitude;
    }

    /**
     * Method setLatitude - Set's a latitude.
     * @param latitude - latitude.
     *
     */
    public void setLatitude(String latitude){
        mLatitude = latitude;
    }

    /**
     * Method getLongitude - Get's longitude.
     * @return mLongitude.
     *
     */
    public String getLongitude(){
        return mLongitude;
    }

    /**
     * Method setLongitude - Set's a longitude.
     * @param longitude - longitude.
     *
     */
    public void setLongitude(String longitude){
        mLongitude = longitude;
    }

    /**
     * Method getCreatorId - Get's creator unique id.
     * @return mCreatorId.
     *
     */
    public String getCreatorId(){
        return mCreatorId;
    }

    /**
     * Method setCreatorId - Set's a creator unique id.
     * @param creatorId - creatorId.
     *
     */
    public void setCreatorId(String creatorId){
        mCreatorId = creatorId;
    }

    /**
     * Method getImageUrl - Get's image url.
     * @return mImageUrl.
     *
     */
    public String getImageUrl(){
        return mImageUrl;
    }

    /**
     * Method setImageUrl - Set's a image url.
     * @param imageUrl - imageUrl.
     *
     */
    public void setImageUrl(String imageUrl){

        mImageUrl = imageUrl;
    }

    /**
     * Method getDate - Get's date.
     * @return mDate.
     *
     */
    public String getDate(){
        return mDate;
    }

    /**
     * Method setDate - Set's a date.
     * @param date - date.
     *
     */
    public void setDate(String date){

        mDate = date;
    }

    /**
     * Method getKey - Get's key.
     * @return mKey.
     *
     */
    @Exclude
    public String getKey(){
        return mKey;
    }

    /**
     * Method setKey - Set's a key.
     * @param key - key.
     *
     */
    @Exclude
    public void setKey(String key){
        mKey = key;
    }
}
