package com.shinhan.review;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class ReviewApplicationTests {


	public int[] solution(int[] array) {
		int[] answer = {};
		int idx = -1, val = -1;
		for(int i=0, len = array.length;i<len;i++){
			if (array[i]>val){
				val = array[i];
				idx = i;
			}
		}
		answer = new int[]{val, idx};
		return answer;
	}

	private boolean isValid(int a, int b, int[] g, int[] s, int[] w, int[] t, long time){
		long gold = 0;
		long silver = 0;
		long total = 0;

		int len = g.length;

		for(int i=0;i<len;i++){
			int base = t[i];
			long rTime = 2 * base;
			long move = time / rTime;
			if (time % rTime >= base){
				move++;
			}
			long maxWork = w[i]*move;
			gold+=Math.min(g[i], maxWork);
			silver+=Math.min(s[i], maxWork);
			total+=Math.min(g[i]+s[i], maxWork);
		}

		if(gold>=a && silver>=b && total>=(a+b))
			return true;
		return false;
	}

	public int solution(int n){
		long[] dp = new long[5001];
		dp[0] = 1;
		dp[1] = 0;
		dp[2] = 3;
		for(int i=3;i<=n;i++){
			if(i%2==1)
				continue;
			dp[i] = 3*(dp[i-2]);
			for(int j=i-4;j>=0;j-=2){
				dp[i] +=(2*dp[j]);
			}

			dp[i]%=1000000007;
		}
		return (int)dp[n];
	}

	public String solution(String s, int n) {
		String answer = "";
		String low = "abcdefghijklmnopqrstuvwxyz";
		String up = low.toUpperCase();
		int baseLen = low.length();

		char[] lower = low.toCharArray();
		char[] upper = up.toCharArray();
		for(int i=0,len=s.length();i<len;i++){
			if (s.charAt(i)==' ')
				answer+=' ';
			else if (low.indexOf(s.charAt(i))!=-1){
				int idx = low.indexOf(s.charAt(i));
				idx+=n;
				answer+=lower[idx%baseLen];
			}else if(up.indexOf(s.charAt(i))!=-1){
				int idx = up.indexOf(s.charAt(i));
				idx+=n;
				answer+=upper[idx%baseLen];
			}
		}
		return answer;
	}








	static class Info implements Comparable<Info>{
		int cost;
		int cur;

		public int getCost() {
			return cost;
		}

		public void setCost(int cost) {
			this.cost = cost;
		}

		public int getCur() {
			return cur;
		}

		public void setCur(int cur) {
			this.cur = cur;
		}

		public Info(int cost, int cur) {
			this.cost = cost;
			this.cur = cur;
		}

		public Info() {
		}

		@Override
		public int compareTo(Info o) {
			return Integer.compare(getCost(), o.getCost());
		}
	}
	public int solution(int n, int[] cores) {
		int answer = 0;

		int left = 0;
		int right = (int)2e6;

		while(left<right){
			int mid = (left+right)>>1;
			int cnt = 0;
			int av = 0;
			for(int core : cores){
				cnt+=mid/core;
				if(mid%core==0){
					cnt--;
					av++;
				}
			}

			if(cnt>=n){
				right = mid;
			}else if(cnt+av<n){
				left = mid+1;
			}else{
				int idx = 0;
				for(int core : cores){
					if(mid%core==0)
						cnt++;
					if(cnt==n)
						return idx+1;

					idx++;
				}

			}
		}
		return answer;
	}


	boolean[] isSummit = new boolean[50001];
	List<Info>[] edge = new ArrayList[50001];
	int[] intensity = new int[50001];
	public int[] solution(int n, int[][] paths, int[] gates, int[] summits) {
		int[] answer = {};
		Arrays.fill(intensity,(int)1e8);

		for (int summit : summits) {
			isSummit[summit] = true;
		}

		for (int[] path : paths) {
			edge[path[0]].add(new Info(path[2], path[1]));
			edge[path[1]].add(new Info(path[2], path[0]));
		}

		int ans = (int)1e9;
		int summit = (int)1e9;

		PriorityQueue<Info> pq = new PriorityQueue<>();
		for (int gate : gates) {
			intensity[gate] = -1;
			pq.add(new Info(0, gate));
		}

		while(!pq.isEmpty()){

			Info it = pq.poll(); // 반환하고 삭제

			if (it.getCost()>ans)
				continue;

			if (isSummit[it.getCur()]) // 봉우리면
			{
				if (it.getCost()<ans){
					ans = it.getCost();
					summit = it.getCur();
				}else if(it.getCost() == ans && it.getCur() < summit){
					summit = it.getCur();
				}
				continue;
			}

			for(Info next : edge[it.getCur()]){
				if (intensity[next.getCur()] > Math.max(next.getCost(), it.getCost())){
					intensity[next.getCur()] = Math.max(next.getCost(), it.getCost());
					pq.add(new Info(intensity[next.getCur()], next.getCur()));
				}
			}
		}

		answer = new int[]{summit, ans};
		return answer;
	}


	public long solution(int a, int b, int[] g, int[] s, int[] w, int[] t) {
		long answer = -1;
		long left = 0;
		long right = (long)10e14*3;
		answer = right;
		while(left<=right){
			long mid = (left+right)>>1;
			if(isValid(a, b, g, s, w, t, mid)){
				answer = Math.min(answer, mid);
				right = mid-1;
			}else
				left = mid+1;
		}
		return answer;
	}

	@Test
	void contextLoads() {
		long solution = solution(90, 500, new int[]{70, 70, 0}, new int[]{0, 0, 500}, new int[]{100, 100, 2}, new int[]{4, 8, 1});
		System.out.println("solution = " + solution);
	}

	@Test
	void tileTest() {
		int solution = solution(4);
		System.out.println("solution = " + solution);
	}
}
