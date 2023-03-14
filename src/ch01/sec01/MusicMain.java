package ch01.sec01;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MusicMain {
	public static final int ADDSONG = 1, INFOSONG = 2, INFO_ALLSONG = 3, DELETESONG = 4, DELETE_ALLSONG = 5,
			UPDATESONG = 6, SORT = 7, PLAYLIST = 8, EXIT = 9;
	public static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		int no = 0;
		boolean run = true;

		ArrayList<Music> mlist = new ArrayList<Music>();
		DBConnection dbCon = new DBConnection();
		
		ArrayList<Music> playlist = new ArrayList<Music>();
		/* °æ·Î ¼³Á¤ÇØ¼­ À½¾Ç Àç»ý ½ÃÅ°±â
		playlist.add(new Music("³ë·¡ Á¦¸ñ", "°¡¼ö", 100,"°æ·Î"));
		*/
		MusicPlayer mp = new MusicPlayer(playlist);

		while (run) {
			System.out.println("================================\t Music Playlist \t================================");
			System.out.println("[1]³ë·¡Ãß°¡ [2]³ë·¡ Á¤º¸ º¸±â [3]ÀüÃ¼ ³ë·¡ Á¤º¸ [4]³ë·¡ »èÁ¦ [5]ÀüÃ¼ ³ë·¡ »èÁ¦ [6]¼öÁ¤ [7]Á¤·Ä [8]Àç»ý ¸ñ·Ï [9]Á¾·á");
			System.out.println("================================================================================================");
			System.out.print("¼±ÅÃ > ");
			no = Integer.parseInt(sc.nextLine());
			switch (no) {
			case ADDSONG:
				Music music = inputDataMusic(mlist);
				// µ¥ÀÌÅÍº£ÀÌ½º ÀÔ·Â
				int rValue = dbCon.insert(music);
				if (rValue == 1) {
					System.out.println("»ðÀÔ¼º°ø");
				} else {
					System.out.println("»ðÀÔ½ÇÆÐ");
				}
				break;
			case INFOSONG:
				String dataTitle = searchSong();
				ArrayList<Music> list1 = dbCon.songSearchSelect(dataTitle);
				if (list1.size() >= 1) {
					printAllSong(list1);
				} else {
					System.out.println("³ë·¡Á¦¸ñ °Ë»ö ¿À·ù");
				}
				break;
			case INFO_ALLSONG:
				ArrayList<Music> mlist2 = dbCon.select();
				if (mlist2 != null) {
					printAllSong(mlist2);
				} else {
					System.out.println("Ãâ·Â ½ÇÆÐ");
				}
				break;
			case DELETESONG:
				int deleteId = inputId();
				int deleteReturnValue = dbCon.delete(deleteId);
				if (deleteReturnValue == 1) {
					System.out.println("»èÁ¦ ¼º°ø");
				} else {
					System.out.println("»èÁ¦ ½ÇÆÐ");
				}
				break;
			case DELETE_ALLSONG:
				System.out.println("---------- ÀüÃ¼ ³ë·¡ »èÁ¦ ----------");
				System.out.println("³ë·¡°¡ ÀüºÎ »èÁ¦ µÇ¾ú½À´Ï´Ù.");
				dbCon.deleteAll();
				break;
			case UPDATESONG:
				int updateReturnValue = 0;
				int id = inputId();
				Music mlt = dbCon.selectId(id);
				if (mlt == null) {
					System.out.println("¼öÁ¤¿À·ù ¹ß»ý");
				} else {
					Music updateSong = updateSong(mlt);
					updateReturnValue = dbCon.update(updateSong);
				}
				if (updateReturnValue == 1) {
					System.out.println("update ¼º°ø");
				} else {
					System.out.println("update ½ÇÆÐ");
				}
				break;
			case SORT:
				ArrayList<Music> list3 = dbCon.selectSort();
				if (list3 == null) {
					System.out.println("Á¤·Ä ½ÇÆÐ");
				} else {
					printAllSong(list3);
					System.out.println("Á¤·Ä ¿Ï·á");
				}
				break;
			case PLAYLIST:
				boolean onOff = true;
				while (onOff) {
					System.out.println("=========================\t Music Play \t=========================");
					System.out.println("[1]Àç»ý  [2]Á¤Áö  [3]ÀÌÀü°î  [4]´ÙÀ½°î  [5]Á¾·á");
					System.out.println("=================================================================================");
					switch (new Scanner(System.in).nextInt()) {
					case 1:
						Music currentMusic = mp.play();
						System.out.println(currentMusic.getTitle() + " Àç»ýÁßÀÔ´Ï´Ù.");
						break;
					case 2:
						mp.stop();
						System.out.println("Á¤ÁöÇÕ´Ï´Ù.");
						break;
					case 3:
						Music previouMusic = mp.previous();
						System.out.println(previouMusic.getTitle() + " Àç»ýÁßÀÔ´Ï´Ù.");
						break;
					case 4:
						Music nextMusic = mp.next();
						System.out.println(nextMusic.getTitle() + " Àç»ýÁßÀÔ´Ï´Ù.");
						break;
					case 5:
						System.out.println("Ã³À½ ¸Þ´º·Î µ¹¾Æ°©´Ï´Ù");
						onOff = false;
						break;
					}
				}
				break;
			case EXIT:
				run = false;
				break;
			}

		} // end of mains
		System.out.println("Á¾·áÇÕ´Ï´Ù.");
	}

	private static Music updateSong(Music mlist) {
		String title = inputSubject(mlist.getTitle() + ">>");
		mlist.setTitle(title);
		String singer = inputSubject(mlist.getSinger() + ">>");
		mlist.setSinger(singer);
		String genre = inputSubject(mlist.getGenre() + ">>");
		mlist.setGenre(genre);
		return mlist;
	}

	private static String inputSubject(String title) {
		boolean run = true;
		String data = null;
		while (run) {
			System.out.print(title + ">>");
			try {
				data = sc.nextLine();
				Pattern pattern = Pattern.compile("^[°¡-ÆR]{1,10}$");
				Matcher matcher = pattern.matcher(data);
				if (matcher.find()) {
					run = false;
				} else {
					System.out.println("Àß¸øÀÔ·ÂÇÏ¿´½À´Ï´Ù. ÀçÀÔ·Â¿äÃ»");
				}
			} catch (NumberFormatException e) {
				System.out.println("ÀÔ·Â¿¡ ¿À·ù ¹ß»ý");
				data = null;
			}
		}
		return data;
	}

	private static int inputId() {
		boolean run = true;
		int id = 0;
		while (run) {
			try {
				System.out.print("ID ÀÔ·Â(number): ");
				id = Integer.parseInt(sc.nextLine());
				if (id > 0 && id < Integer.MAX_VALUE) {
					run = false;
				}
			} catch (NumberFormatException e) {
				System.out.println("id ÀÔ·Â ¿À·ù");
			}
		}
		return id;
	}

	private static Music inputDataMusic(ArrayList<Music> mlist) {
		String title = null;
		String singer = null;
		String genre = null;
		System.out.println("----- ³ë·¡ Ãß°¡ -----");
		System.out.print("Á¦¸ñ ÀÔ·Â :");
		title = sc.nextLine();
		System.out.print("°¡¼ö ÀÔ·Â :");
		singer = sc.nextLine();
		System.out.print("Àå¸£ ÀÔ·Â :");
		genre = sc.nextLine();
		Music music = new Music(title, singer, genre);
		mlist.add(music);
		System.out.println("³ë·¡ Ãß°¡°¡ ¿Ï·áµÇ¾ú½À´Ï´Ù.");
		return music;
	}

	private static void printAllSong(ArrayList<Music> mlist) {
		System.out.println("---------- ÀüÃ¼ ³ë·¡ Á¤º¸ ----------");
		System.out.println("ID" + "\t" + "Á¦¸ñ" + "\t" + "°¡¼ö" + "\t" + "Àå¸£");
		for (Music data : mlist) {
			System.out.print(data.getId() + "\t");
			System.out.print(data.getTitle() + "\t");
			System.out.print(data.getSinger() + "\t");
			System.out.print(data.getGenre() + "\t");
			System.out.println();
		}
	}

	private static String searchSong() {
		String title = null;
		title = matchingTitlePattern();
		return title;
	}

	private static String matchingTitlePattern() {
		String title = null;
		while (true) {
			try {
				System.out.print("°Ë»öÇÒ ³ë·¡ Á¦¸ñ : ");
				title = sc.nextLine();
				Pattern pattern = Pattern.compile("^[°¡-ÆR]{1,10}$");
				Matcher matcher = pattern.matcher(title);
				if (!matcher.find()) {
					System.out.println("Á¦¸ñÀÔ·Â¿À·ù¹ß»ý ´Ù½ÃÀçÀÔ·Â¿äÃ»ÇÕ´Ï´Ù.");
				} else {
					break;
				}
			} catch (Exception e) {
				System.out.println("ÀÔ·Â¿¡¼­ ¿À·ù°¡ ¹ß»ýÇß½À´Ï´Ù.");
				break;
			}

		}
		return title;
	}

}
