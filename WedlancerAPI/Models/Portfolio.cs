using System;
using System.Collections.Generic;

// Code scaffolded by EF Core assumes nullable reference types (NRTs) are not used or disabled.
// If you have enabled NRTs for your project, then un-comment the following line:
// #nullable disable

namespace WedlancerAPI.Models
{
    public partial class Portfolio
    {
        public int PortfolioId { get; set; }
        public int ProfileId { get; set; }
        public string Image { get; set; }
        public string Video { get; set; }

        public virtual Profiles Profile { get; set; }
    }
}
