# Opis
Generator sztucznych wielowymiarowych i wieloklasowych danych niezrównoważonych został zrealizowany w ramach [projektu badawczego NCN 2013/11/B/ST6/00963](http://www.cs.put.poznan.pl/jstefanowski/projNCN.htm).  Został on stworzony z myślą o przygotowywaniu zbiorów do eksperymentów obliczeniowych pozwalających na analizę wpływu różnych czynników trudności związanych z danymi na działanie metod wstępnego przetwarzania oraz klasyfikatorów.

Obecna wersja generatora oferuje następujące możliwości i funkcje:
 * generowanie zbiorów wielowymiarowych i wieloklasowych (z praktycznego punktu widzenia stosowane są 3-4 klasy oraz 3-10 atrybutów warunkowych odpowiadających poszczególnym wymiarom),
* definiowanie klas składających się z jednego lub wielu obszarów (regionów) – meta-kul oraz meta-kostek z możliwością wskazania klas „wypełniających” dostępną przestrzeń między regionami (takie wypełnienia zazwyczaj związane z klasami większościowymi),
* możliwość różnicowania względnego rozkładu obiektów między regionami oraz kontrolowania „gęstości” obiektów wewnątrz poszczególnych regionów (wybór jednego z dwóch możliwych rozkładów obiektów – jednostajnego i normalnego),* generowanie obiektów następujących typów:
    * _safe_ – obiekty „bezpieczne” leżące wewnątrz poszczególnych regionów i posiadające jednorodne sąsiedztwo składające się głównie z obiektów tej samej klasy,</li>
    * _borderline_ (lub _border_) – obiekty brzegowe leżące na obrzeżu poszczególnych regionów i przemieszane z obiektami z innych klas,
    * _rare_ – obiekty tworzące małe „wyspy” (składające się z 2-3 obiektów) leżące daleko od regionów tworzących daną klasę, odpowiadające one rzadkim, ale poprawnym obserwacjom,
    * _outlier_ – pojedyncze obiekty leżące daleko od innych obiektów z tej samej klasy,odpowiadające obserwacjom „odstającym”,
* generowanie par zbiorów uczący-testujący z zachowaniem położenia obiektów typu _rare_ i _outlier_ (w poszczególnych parach zbiorów powinny one występować w zbliżonych lokalizacjach)

Generator został zaimplementowany w języku Java i pozwla na zapis danych w formacie ARFF wykorzystywanym w środowisku WEKA. Umożliwia również eksport danych do formatu CSV w celu ich łatwieszego przetwarzania przez inne narzędzia do analizy danych.

# Publikacje

1. Sz. Wojciechowski, Sz. Wilk: [Generator sztucznych danych wielowymiarowych - projekt i implementacja](./raport-rb-16-14.pdf). Raport Badawczy RB-16/14. Politechnika Poznańska, 2014.
2. Sz. Wojciechowski, Sz. Wilk: [Generator sztucznych danych wielowymiarowych - weryfikacja eksperymentalna](./raport-rb-2-15.pdf). Raport Badawczy RB-2/15. Politechnika Poznańska, 2015.
3. Sz. Wojciechowski, Sz. Wilk: [Difficulty factors and preprocessing in imbalanced data sets: an experimental study on artificial data](https://www.degruyter.com/downloadpdf/j/fcds.2017.42.issue-2/fcds-2017-0007/fcds-2017-0007.xml). Foundations of Computing and Decision Sciences 42 (2), 2017, 149-176.

# Do pobrania

1. Najnowsza skompilowna wersja generatora (2017.08.10) wraz z niezbędnymi bibliotekami [[ZIP, 4.5 MB](./datagenerator-20170810.zip)]. Szczegółowy opis parametryzacji oraz uruchamiania generatora zawarty jest w raporcie RB-16/14.
2. Źródła [[GitHub](https://github.com/sysmon37/datagenerator)]
