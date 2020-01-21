const sut = require("./example.js");

describe("statement", () => {
  it("should generate results", () => {
    expect(
      sut.invoices.map(invoice => sut.statement(invoice, sut.plays))
    ).toEqual([
      `Statement for BigCo
  Hamlet: $650.00 (55 seats)
  As You Like It: $580.00 (35 seats)
  Othello: $500.00 (40 seats)
Amount owed is $1,730.00
You earned 47 credits
`
    ]);
  });
});
