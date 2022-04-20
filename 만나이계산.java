// 주민등록번호 == Resident Registration Number
    public static int calculateAge(String rrn)
    {
        // 주민등로번호가 123456-1234567 이라고 가정함
        int age = 0;
        
        rrn = rrn.replaceAll("-","");
        if(rrn.length()<7) // 잘못된 주민등록번호
            return 0;
        
        String curTime = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        
        curTime = format.format(new Date());
        System.out.println("curDay = " + curTime);
        
        int curYear = Integer.parseInt(curTime.substring(0, 4));
        int curMonth = Integer.parseInt(curTime.substring(4, 6));
        int curDay = Integer.parseInt(curTime.substring(6, 8));
        
        int rrnYear = Integer.parseInt(rrn.substring(0, 2));
        int rrnMonth = Integer.parseInt(rrn.substring(2, 4));
        int rrnDay = Integer.parseInt(rrn.substring(4, 6));

        char sevenValue = rrn.charAt(6);

        if(sevenValue=='0' || sevenValue=='9')
        {
            // 1800년대 사람
            rrnYear+=1800;
        }else if(sevenValue=='1' || sevenValue=='2'||sevenValue=='5'||sevenValue=='6')
        {
            rrnYear+=1900;
        }else if(sevenValue=='7' || sevenValue=='8')
        {
            int foreignAge = curYear%1000+1;
            if(rrnYear>=0 && rrnYear<foreignAge)
            {
                rrnYear+=2000;
            }else
            {
                rrnYear+=1900;
            }
        }else
        {
            rrnYear+=2000;
        }
        
        age = curYear-rrnYear;
        
        // 생일 지났는지 체크
        
        if(rrnMonth>curMonth)
        {
            age--;
        }else if(rrnMonth==curMonth)
        {
            if(rrnDay>curDay)
            {
                age--;
            }
        }
        
        
        return age;
    }
