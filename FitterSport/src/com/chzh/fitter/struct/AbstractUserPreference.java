package com.chzh.fitter.struct;

import com.chzh.fitter.data.AbstractPreferencesData;
@Deprecated //用 JUser
public abstract class AbstractUserPreference<T extends AbstractUserPreference<T>> extends
		AbstractPreferencesData {

	private String portrait;

	private String nick;

	private String mobile;

	private int gender;

	private float height;

	private float weight;

	private String birthday;

	private float bmi;

	private String token;

	private int age;


	public String getAvatarUrl() {
		return portrait;
	}

	public T avatarUrl(String avatarUrl) {
		this.portrait = avatarUrl;
		return self();
	}

	public String getNick() {
		return nick;
	}

	public T nick(String nick) {
		this.nick = nick;
		return self();
	}

	public String getAccount() {
		return mobile;
	}

	public T account(String account) {
		this.mobile = account;
		return self();
	}

	public int getGender() {
		return gender;
	}

	public T gender(Integer gender) {
		this.gender = gender;
		return self();
	}

	public float getHeight() {
		return height;
	}

	public T height(Float height) {
		this.height = height;
		return self();
	}

	public float getWeight() {
		return weight;
	}

	public T weight(Float weight) {
		this.weight = weight;
		return self();
	}

	public String getBirthday() {
		return birthday;
	}

	public T birthday(String birthday) {
		this.birthday = birthday;
		return self();
	}

	public float getBmi() {
		return bmi;
	}

	public T bmi(Float bmi) {
		this.bmi = bmi;
		return self();
	}

	@SuppressWarnings("unchecked")
	protected T self() {
		return (T) this;
	}

	public String getToken() {
		return token;
	}

	public T token(String token) {
		this.token = token;
		return self();
	}

	public int getAge() {
		return age;
	}

	public T age(Integer age) {
		this.age = age;
		return self();
	}
}
