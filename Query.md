> Oracle paging query

select * from mbi_simpleauth order by reg_dttm desc offset 0 rows fetch next 3 rows only;
0번째 인덱스로부터 총 3개 가져옴.
