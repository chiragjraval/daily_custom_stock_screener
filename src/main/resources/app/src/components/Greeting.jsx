import React from "react";

function Greeting({ name }) {
  return (
      <div className="container-fluid mt-4">
        This screener was built by <strong>{name}</strong> 🙂using <strong>Screener Link</strong> <a href="https://www.screener.in/screens/2618573/companies-with-growth-salesprofitmargin">https://www.screener.in/screens/2618573/companies-with-growth-salesprofitmargin</a>
      </div>
  );
}

export default Greeting;
