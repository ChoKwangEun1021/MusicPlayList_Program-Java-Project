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
		/* 경로 설정해서 음악 재생 시키기
		playlist.add(new Music("노래 제목", "가수", 100,"경로"));
		*/
		MusicPlayer mp = new MusicPlayer(playlist);

		while (run) {
			System.out.println("================================\t Music Playlist \t================================");
			System.out.println("[1]노래추가 [2]노래 정보 보기 [3]전체 노래 정보 [4]노래 삭제 [5]전체 노래 삭제 [6]수정 [7]정렬 [8]재생 목록 [9]종료");
			System.out.println("================================================================================================");
			System.out.print("선택 > ");
			no = Integer.parseInt(sc.nextLine());
			switch (no) {
			case ADDSONG:
				Music music = inputDataMusic(mlist);
				// 데이터베이스 입력
				int rValue = dbCon.insert(music);
				if (rValue == 1) {
					System.out.println("삽입성공");
				} else {
					System.out.println("삽입실패");
				}
				break;
			case INFOSONG:
				String dataTitle = searchSong();
				ArrayList<Music> list1 = dbCon.songSearchSelect(dataTitle);
				if (list1.size() >= 1) {
					printAllSong(list1);
				} else {
					System.out.println("노래제목 검색 오류");
				}
				break;
			case INFO_ALLSONG:
				ArrayList<Music> mlist2 = dbCon.select();
				if (mlist2 != null) {
					printAllSong(mlist2);
				} else {
					System.out.println("출력 실패");
				}
				break;
			case DELETESONG:
				int deleteId = inputId();
				int deleteReturnValue = dbCon.delete(deleteId);
				if (deleteReturnValue == 1) {
					System.out.println("삭제 성공");
				} else {
					System.out.println("삭제 실패");
				}
				break;
			case DELETE_ALLSONG:
				System.out.println("---------- 전체 노래 삭제 ----------");
				System.out.println("노래가 전부 삭제 되었습니다.");
				dbCon.deleteAll();
				break;
			case UPDATESONG:
				int updateReturnValue = 0;
				int id = inputId();
				Music mlt = dbCon.selectId(id);
				if (mlt == null) {
					System.out.println("수정오류 발생");
				} else {
					Music updateSong = updateSong(mlt);
					updateReturnValue = dbCon.update(updateSong);
				}
				if (updateReturnValue == 1) {
					System.out.println("update 성공");
				} else {
					System.out.println("update 실패");
				}
				break;
			case SORT:
				ArrayList<Music> list3 = dbCon.selectSort();
				if (list3 == null) {
					System.out.println("정렬 실패");
				} else {
					printAllSong(list3);
					System.out.println("정렬 완료");
				}
				break;
			case PLAYLIST:
				boolean onOff = true;
				while (onOff) {
					System.out.println("=========================\t Music Play \t=========================");
					System.out.println("[1]재생  [2]정지  [3]이전곡  [4]다음곡  [5]종료");
					System.out.println("=================================================================================");
					switch (new Scanner(System.in).nextInt()) {
					case 1:
						Music currentMusic = mp.play();
						System.out.println(currentMusic.getTitle() + " 재생중입니다.");
						break;
					case 2:
						mp.stop();
						System.out.println("정지합니다.");
						break;
					case 3:
						Music previouMusic = mp.previous();
						System.out.println(previouMusic.getTitle() + " 재생중입니다.");
						break;
					case 4:
						Music nextMusic = mp.next();
						System.out.println(nextMusic.getTitle() + " 재생중입니다.");
						break;
					case 5:
						System.out.println("처음 메뉴로 돌아갑니다");
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
		System.out.println("종료합니다.");
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
				Pattern pattern = Pattern.compile("^[가-힣]{1,10}$");
				Matcher matcher = pattern.matcher(data);
				if (matcher.find()) {
					run = false;
				} else {
					System.out.println("잘못입력하였습니다. 재입력요청");
				}
			} catch (NumberFormatException e) {
				System.out.println("입력에 오류 발생");
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
				System.out.print("ID 입력(number): ");
				id = Integer.parseInt(sc.nextLine());
				if (id > 0 && id < Integer.MAX_VALUE) {
					run = false;
				}
			} catch (NumberFormatException e) {
				System.out.println("id 입력 오류");
			}
		}
		return id;
	}

	private static Music inputDataMusic(ArrayList<Music> mlist) {
		String title = null;
		String singer = null;
		String genre = null;
		System.out.println("----- 노래 추가 -----");
		System.out.print("제목 입력 :");
		title = sc.nextLine();
		System.out.print("가수 입력 :");
		singer = sc.nextLine();
		System.out.print("장르 입력 :");
		genre = sc.nextLine();
		Music music = new Music(title, singer, genre);
		mlist.add(music);
		System.out.println("노래 추가가 완료되었습니다.");
		return music;
	}

	private static void printAllSong(ArrayList<Music> mlist) {
		System.out.println("---------- 전체 노래 정보 ----------");
		System.out.println("ID" + "\t" + "제목" + "\t" + "가수" + "\t" + "장르");
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
				System.out.print("검색할 노래 제목 : ");
				title = sc.nextLine();
				Pattern pattern = Pattern.compile("^[가-힣]{1,10}$");
				Matcher matcher = pattern.matcher(title);
				if (!matcher.find()) {
					System.out.println("제목입력오류발생 다시재입력요청합니다.");
				} else {
					break;
				}
			} catch (Exception e) {
				System.out.println("입력에서 오류가 발생했습니다.");
				break;
			}

		}
		return title;
	}

}
