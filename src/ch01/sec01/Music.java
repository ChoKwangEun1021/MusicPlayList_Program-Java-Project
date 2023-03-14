package ch01.sec01;

import java.util.Objects;

public class Music {
	private int id;
	private String title;
	private String singer;
	private String genre;
	private int playTime;
	private String path;

	public Music() {

	}

	public Music(String title, String singer, String genre) {
		this(0, title, singer, genre, 0, null);
	}

	public Music(int id, String title, String singer, String genre) {
		this(id, title, singer, genre, 0, null);
	}
	
	public Music(String title, String singer, int playTime, String path) {
		this(0, title, singer, null, 0, null);
		this.title = title;
		this.singer = singer;
		this.playTime = playTime;
		this.path = path;
	}
	public Music(int id, String title, String singer, String genre, int playTime, String path) {
		super();
		this.id = id;
		this.title = title;
		this.singer = singer;
		this.genre = genre;
		this.playTime = playTime;
		this.path = path;
	}
	
	public int getPlayTime() {
		return playTime;
	}

	public void setPlayTime(int playTime) {
		this.playTime = playTime;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.title);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Music) {
			Music music = (Music) obj;
			return (this.title.equals(music.title));
		}
		return false;
	}

	@Override
	public String toString() {
		return id + "\t" + title + "\t" + singer + "\t" + genre;
	}

}
