<!--
 ___ _            _ _    _ _    __
/ __(_)_ __  _ __| (_)__(_) |_ /_/
\__ \ | '  \| '_ \ | / _| |  _/ -_)
|___/_|_|_|_| .__/_|_\__|_|\__\___|
            |_| 
-->
![](https://docs.simplicite.io//logos/logo250.png)
* * *

`Trello2Redmine` module definition
==================================



`TrdConfig` business object definition
--------------------------------------



### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      | 
| ------------------------------------------------------------ | ---------------------------------------- | -------- | --------- | -------- | -------------------------------------------------------------------------------- |
| `trdCgfTrelloBoard`                                          | char(255)                                | yes      | yes       |          | -                                                                                |
| `trdCgfTrelloToken`                                          | password(255)                            | yes      | yes       |          | -                                                                                |
| `trdCgfTrelloKey`                                            | password(255)                            | yes      | yes       |          | -                                                                                |
| `trdCgfRedmineKey`                                           | password(255)                            | yes      | yes       |          | -                                                                                |
| `trdCgfRedmineUser`                                          | int(3)                                   | yes      | yes       |          | -                                                                                |
| `trdCgfRedmineProject`                                       | int(3)                                   | yes      | yes       |          | -                                                                                |
| `trdCgfDescription`                                          | char(255)                                | yes*     | yes       |          | -                                                                                |
| `trdCgfRedmineInstanceUrl`                                   | url(255)                                 |          | yes       |          | -                                                                                |

### Custom actions

* `import`: 

