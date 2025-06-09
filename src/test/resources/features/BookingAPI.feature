@BookingAll
Feature: APIS from Booking

  Como usuario de Booking
  Quiero obtener la lista de bookings
  Para poder verificar los detalles de los registros

  @CP01_Booking @Booking
  Scenario: Obtener todas los booking exitosamente
    Given el actor establece el endpoint de booking
    When el actor realiza una solicitud GET
    Then el codigo de respuesta debe ser 200


  @CP02_Booking @Patch_Booking @Booking
  Scenario Outline: Crear un booking exitosamente
    Given el actor establece el endpoint de booking
    When el actor crea un booking con el "<firstname>" "<lastname>" "<totalprice>" "<depositpaid>" "<checkin>" "<checkout>" "<additionalneeds>"
    Then el codigo de respuesta debe ser 200

    Examples:
      | firstname | lastname | totalprice | depositpaid | checkin    | checkout   | additionalneeds |
      | Mario     | Castilla | 111        | true        | 2018-01-01 | 2019-01-01 | Breakfast       |
      #| Roberto   | Alfredo  | 222        | true        | 2018-01-01 | 2019-01-01 | Breakfast       |




  @CP03_Booking @Patch_Booking @Booking

  Scenario Outline: Actualizar datos de un booking exitosamente
    Given el actor establece el endpoint de booking
    And el actor crea un booking con el "Ada" "Wong" "111" "true" "checkin" "checkout" "Run"
    When el actor actualiza un booking con los datos "<firstname>" "<lastname>" "<totalprice>"
    Then el codigo de respuesta debe ser 200

    Examples:
      | firstname | lastname  | totalprice |
      | Carlos    | Alcantara | 999        |




  @CP04_Booking @Put_Booking @Booking
  Scenario: Actualizar un booking usando PUT
    Given el actor establece el endpoint de booking
    And el actor crea un booking con el "Enrique" "Jose" "500" "true" "2023-01-01" "2023-01-05" "Breakfast"
    When el actor actualiza un booking usando PUT con los datos "Jane" "Smith" 750
    Then el codigo de respuesta debe ser 200




  @CP05_Booking @Delete_Booking @Booking
  Scenario: Eliminar un booking usando DELETE
    Given el actor establece el endpoint de booking
    And el actor crea un booking con el "Jose" "Enrique" "300" "true" "2023-02-01" "2023-02-07" "Lunch"
    When el actor elimina un booking usando DELETE
    Then el codigo de respuesta debe ser 201
