# LogsApp
Logs Task

The project correctly identifies jobs which took less than 5 minutes which I considered to be marked as "COMPLETED",
jobs which take between, 5 to 10 minutes, marked as "WARNING", and jobs which last longer than 10 minutes, correctly
marked with "ERROR" status.

The project has a robust testing suit for validating the input thoroughly and for testing the actual output is 
indeed the expected desired output. Tests are written following the Gherkin paradigm (given-when-then). 
In addition, parameterized unit tests are used when suitable. 

In the beginning I followed TDD by creating tests for validating the input values and patterns, including duplicate entries, inconsistent START and END times
for given PIDs etc.

The output is of the following form:

```
Time taken for sequential processing: 47 ms
Job Report
---------------------------------------------------------
| Job Description            | PID    | Start Time | End Time  | Duration | Status    |
---------------------------------------------------------
| scheduled task 936       | 62401  | 12:05:59   | N/A        | PT10M24S | ERROR     |
| scheduled task 936       | 62401  | N/A        | 12:16:23   | PT10M24S | ERROR     |
| scheduled task 697       | 32904  | 11:49:12   | N/A        | PT34S    | COMPLETED |
| scheduled task 697       | 32904  | N/A        | 11:49:46   | PT34S    | COMPLETED |
| scheduled task 796       | 57672  | 11:36:11   | N/A        | PT7S     | COMPLETED |
| scheduled task 796       | 57672  | N/A        | 11:36:18   | PT7S     | COMPLETED |
| scheduled task 531       | 62922  | 12:14:20   | N/A        | PT49S    | COMPLETED |
| scheduled task 531       | 62922  | N/A        | 12:15:09   | PT49S    | COMPLETED |
| scheduled task 920       | 34189  | 11:59:43   | N/A        | PT3M19S  | COMPLETED |
| scheduled task 920       | 34189  | N/A        | 12:03:02   | PT3M19S  | COMPLETED |
| scheduled task 188       | 23118  | 11:40:49   | N/A        | PT39S    | COMPLETED |
| scheduled task 188       | 23118  | N/A        | 11:41:28   | PT39S    | COMPLETED |
| scheduled task 521       | 64591  | 11:57:05   | N/A        | PT1M50S  | COMPLETED |
| scheduled task 521       | 64591  | N/A        | 11:58:55   | PT1M50S  | COMPLETED |
| scheduled task 538       | 26831  | 11:46:04   | N/A        | PT2M12S  | COMPLETED |
| scheduled task 538       | 26831  | N/A        | 11:48:16   | PT2M12S  | COMPLETED |
| scheduled task 515       | 45135  | 11:37:14   | N/A        | PT12M23S | ERROR     |
| scheduled task 515       | 45135  | N/A        | 11:49:37   | PT12M23S | ERROR     |
| scheduled task 794       | 87570  | 11:53:57   | N/A        | PT7M53S  | WARNING   |
| scheduled task 794       | 87570  | N/A        | 12:01:50   | PT7M53S  | WARNING   |
| scheduled task 996       | 90962  | 11:40:51   | N/A        | PT1M55S  | COMPLETED |
| scheduled task 996       | 90962  | N/A        | 11:42:46   | PT1M55S  | COMPLETED |
| scheduled task 386       | 10515  | 11:38:33   | N/A        | PT1M51S  | COMPLETED |
| scheduled task 386       | 10515  | N/A        | 11:40:24   | PT1M51S  | COMPLETED |
| scheduled task 294       | 27222  | 11:50:07   | N/A        | PT6M8S   | WARNING   |
| scheduled task 294       | 27222  | N/A        | 11:56:15   | PT6M8S   | WARNING   |
| scheduled task 374       | 23703  | 12:04:57   | N/A        | PT13M26S | ERROR     |
| scheduled task 374       | 23703  | N/A        | 12:18:23   | PT13M26S | ERROR     |
| scheduled task 074       | 71766  | 11:45:04   | N/A        | PT5M47S  | WARNING   |
| scheduled task 074       | 71766  | N/A        | 11:50:51   | PT5M47S  | WARNING   |
| background job sqm       | 99672  | 11:57:08   | N/A        | PT5M13S  | WARNING   |
| background job sqm       | 99672  | N/A        | 12:02:21   | PT5M13S  | WARNING   |
| scheduled task 182       | 70808  | 11:44:43   | N/A        | PT33M43S | ERROR     |
| scheduled task 182       | 70808  | N/A        | 12:18:26   | PT33M43S | ERROR     |
| scheduled task 032       | 37980  | 11:35:23   | N/A        | PT33S    | COMPLETED |
| scheduled task 032       | 37980  | N/A        | 11:35:56   | PT33S    | COMPLETED |
| scheduled task 173       | 75164  | 11:45:47   | N/A        | PT1M4S   | COMPLETED |
| scheduled task 173       | 75164  | N/A        | 11:46:51   | PT1M4S   | COMPLETED |
| scheduled task 536       | 24799  | 11:51:21   | N/A        | PT7S     | COMPLETED |
| scheduled task 536       | 24799  | N/A        | 11:51:28   | PT7S     | COMPLETED |
| scheduled task 672       | 24482  | 12:10:38   | N/A        | PT8M36S  | WARNING   |
| scheduled task 672       | 24482  | N/A        | 12:19:14   | PT8M36S  | WARNING   |
| scheduled task 626       | 32674  | 11:51:06   | N/A        | PT1M26S  | COMPLETED |
| scheduled task 626       | 32674  | N/A        | 11:52:32   | PT1M26S  | COMPLETED |
| scheduled task 946       | 47139  | 11:44:56   | N/A        | PT3M26S  | COMPLETED |
| scheduled task 946       | 47139  | N/A        | 11:48:22   | PT3M26S  | COMPLETED |
| background job djw       | 36709  | 11:47:04   | N/A        | PT50S    | COMPLETED |
| background job djw       | 36709  | N/A        | 11:47:54   | PT50S    | COMPLETED |
| background job ulp       | 60134  | 11:41:11   | N/A        | PT44S    | COMPLETED |
| background job ulp       | 60134  | N/A        | 11:41:55   | PT44S    | COMPLETED |
| scheduled task 773       | 16168  | 12:02:39   | N/A        | PT16S    | COMPLETED |
| scheduled task 773       | 16168  | N/A        | 12:02:55   | PT16S    | COMPLETED |
| background job cmx       | 55722  | 11:54:56   | N/A        | PT47S    | COMPLETED |
| background job cmx       | 55722  | N/A        | 11:55:43   | PT47S    | COMPLETED |
| background job wmy       | 81258  | 11:36:58   | N/A        | PT14M46S | ERROR     |
| background job wmy       | 81258  | N/A        | 11:51:44   | PT14M46S | ERROR     |
| scheduled task 064       | 85742  | 11:55:16   | N/A        | PT12M17S | ERROR     |
| scheduled task 064       | 85742  | N/A        | 12:07:33   | PT12M17S | ERROR     |
| scheduled task 004       | 22003  | 11:55:29   | N/A        | PT11M13S | ERROR     |
| scheduled task 004       | 22003  | N/A        | 12:06:42   | PT11M13S | ERROR     |
| background job you       | 38579  | 11:50:09   | N/A        | PT3M33S  | COMPLETED |
| background job you       | 38579  | N/A        | 11:53:42   | PT3M33S  | COMPLETED |
| background job tqc       | 52532  | 12:00:03   | N/A        | PT13M53S | ERROR     |
| background job tqc       | 52532  | N/A        | 12:13:56   | PT13M53S | ERROR     |
| scheduled task 460       | 39860  | 11:53:17   | N/A        | PT19M52S | ERROR     |
| scheduled task 460       | 39860  | N/A        | 12:13:09   | PT19M52S | ERROR     |
| scheduled task 678       | 96183  | 11:58:12   | N/A        | PT4M14S  | COMPLETED |
| scheduled task 678       | 96183  | N/A        | 12:02:26   | PT4M14S  | COMPLETED |
| scheduled task 811       | 50295  | 11:48:45   | N/A        | PT6M35S  | WARNING   |
| scheduled task 811       | 50295  | N/A        | 11:55:20   | PT6M35S  | WARNING   |
| scheduled task 080       | 67833  | 11:57:16   | N/A        | PT3M35S  | COMPLETED |
| scheduled task 080       | 67833  | N/A        | 12:00:51   | PT3M35S  | COMPLETED |
| scheduled task 706       | 33528  | 11:52:47   | N/A        | PT3M22S  | COMPLETED |
| scheduled task 706       | 33528  | N/A        | 11:56:09   | PT3M22S  | COMPLETED |
| scheduled task 746       | 98746  | 12:04:18   | N/A        | PT7M17S  | WARNING   |
| scheduled task 746       | 98746  | N/A        | 12:11:35   | PT7M17S  | WARNING   |
| scheduled task 051       | 39547  | 11:37:53   | N/A        | PT11M29S | ERROR     |
| scheduled task 051       | 39547  | N/A        | 11:49:22   | PT11M29S | ERROR     |
| background job xfg       | 86716  | 11:59:29   | N/A        | PT5M34S  | WARNING   |
| background job xfg       | 86716  | N/A        | 12:05:03   | PT5M34S  | WARNING   |
| scheduled task 268       | 87228  | 11:44:25   | N/A        | PT9M28S  | WARNING   |
| scheduled task 268       | 87228  | N/A        | 11:53:53   | PT9M28S  | WARNING   |
| background job dej       | 90812  | 11:39:26   | N/A        | PT4M6S   | COMPLETED |
| background job dej       | 90812  | N/A        | 11:43:32   | PT4M6S   | COMPLETED |
| background job wiy       | 81470  | 12:08:30   | N/A        | PT1M3S   | COMPLETED |
| background job wiy       | 81470  | N/A        | 12:09:33   | PT1M3S   | COMPLETED |
---------------------------------------------------------
Total Jobs Processed: 43
```

The project has a checkstyle violation that runs at compile time (mvn clean install) to ensure good coding standards.

Moreover, I tested concurrent implementations for parsing and validating the entries from the logs.log file. 

On branch "LOGS-09" you can find a concurrent implementation of our task using ExecutorService with a fixed thread pool of size 4.
To test the performance I created a custom 'LogGenerator' that generates a customizable number of jobs consistent with our input and stores them
in a log file called massive_logs.log. For 1000 entries and a thread pool of 4, on a MacOS Intel with 4 cores, the process took on average 
80 ms in comparison to the sequential implementation that takes 130 ms. 

Branch called "LOGS-11" has a concurrent implementation that uses a parallel stream. This performs poorly taking 6000 ms on average to finish (yet again for 1000 jobs).

"LOGS-12" branch has a concurrent implementation using the ForkJoin framework. Based on my tests this performs well taking just under 80 ms for 1000 parsed jobs,
making it a competitor for the Executor Service implementation. We could also test when splitting into multiple chunks of data from the input log file and launching more threads 
in execution for each chunk.

The number of worker threads that ought to be launched into execution and then synchronised depend on the number of cores the machine on which the application is tested 
and on the input size. These furthermore have direct correlation with the execution time. 

The concurrent implementations from the indicated branches are not merged with main, but please feel free to detach on the specified branches and test my code.