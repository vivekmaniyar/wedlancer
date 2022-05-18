using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;

// Code scaffolded by EF Core assumes nullable reference types (NRTs) are not used or disabled.
// If you have enabled NRTs for your project, then un-comment the following line:
// #nullable disable

namespace WedlancerAPI.Models
{
    public partial class WedlancerContext : DbContext
    {
        public WedlancerContext()
        {
            
        }

        public WedlancerContext(DbContextOptions<WedlancerContext> options)
            : base(options)
        {
        }

        public virtual DbSet<Bookings> Bookings { get; set; }
        public virtual DbSet<Categories> Categories { get; set; }
        public virtual DbSet<Cities> Cities { get; set; }
        public virtual DbSet<Inquiries> Inquiries { get; set; }
        public virtual DbSet<Messages> Messages { get; set; }
        public virtual DbSet<Orders> Orders { get; set; }
        public virtual DbSet<Packages> Packages { get; set; }
        public virtual DbSet<Portfolio> Portfolio { get; set; }
        public virtual DbSet<Profiles> Profiles { get; set; }
        public virtual DbSet<Reviews> Reviews { get; set; }
        public virtual DbSet<Roles> Roles { get; set; }
        public virtual DbSet<States> States { get; set; }
        public virtual DbSet<UserRoles> UserRoles { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
#warning To protect potentially sensitive information in your connection string, you should move it out of source code. See http://go.microsoft.com/fwlink/?LinkId=723263 for guidance on storing connection strings.
                optionsBuilder.UseSqlServer("Data Source=LAPTOP-V6IEUERI;Initial Catalog=Wedlancer;Integrated Security=True");
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Bookings>(entity =>
            {
                entity.HasKey(e => e.BookingId);

                entity.Property(e => e.BookingId).HasColumnName("Booking_ID");

                entity.Property(e => e.EmployerId).HasColumnName("Employer_ID");

                entity.Property(e => e.EndDate)
                    .HasColumnName("End_Date")
                    .HasColumnType("datetime");

                entity.Property(e => e.ProfileId).HasColumnName("Profile_ID");

                entity.Property(e => e.StartDate)
                    .HasColumnName("Start_Date")
                    .HasColumnType("datetime");

                entity.Property(e => e.Status)
                    .IsRequired()
                    .HasMaxLength(10)
                    .IsUnicode(false)
                    .IsFixedLength();

                entity.HasOne(d => d.Employer)
                    .WithMany(p => p.BookingsEmployer)
                    .HasForeignKey(d => d.EmployerId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Bookings_Employer");

                entity.HasOne(d => d.Profile)
                    .WithMany(p => p.BookingsProfile)
                    .HasForeignKey(d => d.ProfileId)
                    .HasConstraintName("FK_Bookings_Profiles");
            });

            modelBuilder.Entity<Categories>(entity =>
            {
                entity.HasKey(e => e.CategoryId);

                entity.Property(e => e.CategoryId).HasColumnName("Category_ID");

                entity.Property(e => e.CategoryName)
                    .IsRequired()
                    .HasColumnName("Category_Name")
                    .HasMaxLength(50)
                    .IsUnicode(false);
            });

            modelBuilder.Entity<Cities>(entity =>
            {
                entity.HasKey(e => e.CityId);

                entity.Property(e => e.CityId).HasColumnName("City_ID");

                entity.Property(e => e.CityName)
                    .IsRequired()
                    .HasColumnName("City_Name")
                    .HasMaxLength(50)
                    .IsUnicode(false);

                entity.Property(e => e.StateId).HasColumnName("State_ID");

                entity.HasOne(d => d.State)
                    .WithMany(p => p.Cities)
                    .HasForeignKey(d => d.StateId)
                    .HasConstraintName("FK_Cities_States");
            });

            modelBuilder.Entity<Inquiries>(entity =>
            {
                entity.Property(e => e.Id).HasColumnName("ID");

                entity.Property(e => e.Email).IsUnicode(false);

                entity.Property(e => e.Message)
                    .IsRequired()
                    .IsUnicode(false);

                entity.Property(e => e.Name)
                    .IsRequired()
                    .HasMaxLength(50)
                    .IsUnicode(false);

                entity.Property(e => e.PhoneNumber).HasColumnName("Phone_Number");

                entity.Property(e => e.Status)
                    .IsRequired()
                    .HasMaxLength(10)
                    .IsUnicode(false)
                    .IsFixedLength();
            });

            modelBuilder.Entity<Messages>(entity =>
            {
                entity.HasKey(e => e.MessageId);

                entity.Property(e => e.MessageId).HasColumnName("Message_ID");

                entity.Property(e => e.CreatedOn).HasColumnType("datetime");

                entity.Property(e => e.EmployerId).HasColumnName("Employer_ID");

                entity.Property(e => e.FreelancerId).HasColumnName("Freelancer_ID");

                entity.Property(e => e.Message)
                    .IsRequired()
                    .IsUnicode(false);

                entity.HasOne(d => d.Employer)
                    .WithMany(p => p.MessagesEmployer)
                    .HasForeignKey(d => d.EmployerId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Messages_Empoyer");

                entity.HasOne(d => d.Freelancer)
                    .WithMany(p => p.MessagesFreelancer)
                    .HasForeignKey(d => d.FreelancerId)
                    .HasConstraintName("FK_Messages_Freelancer");
            });

            modelBuilder.Entity<Orders>(entity =>
            {
                entity.HasKey(e => e.OrderId);

                entity.Property(e => e.OrderId).HasColumnName("Order_ID");

                entity.Property(e => e.FreelancerId).HasColumnName("Freelancer_ID");

                entity.Property(e => e.PackageId).HasColumnName("Package_ID");

                entity.Property(e => e.PaymentId)
                    .IsRequired()
                    .HasColumnName("Payment_ID")
                    .IsUnicode(false);

                entity.Property(e => e.Status)
                    .IsRequired()
                    .HasMaxLength(10)
                    .IsUnicode(false)
                    .IsFixedLength();

                entity.HasOne(d => d.Freelancer)
                    .WithMany(p => p.Orders)
                    .HasForeignKey(d => d.FreelancerId)
                    .HasConstraintName("FK_Orders_Profiles");

                entity.HasOne(d => d.Package)
                    .WithMany(p => p.Orders)
                    .HasForeignKey(d => d.PackageId)
                    .HasConstraintName("FK_Orders_Packages");
            });

            modelBuilder.Entity<Packages>(entity =>
            {
                entity.HasKey(e => e.PackageId);

                entity.Property(e => e.PackageId).HasColumnName("Package_ID");

                entity.Property(e => e.Description)
                    .IsRequired()
                    .IsUnicode(false);

                entity.Property(e => e.PackageName)
                    .IsRequired()
                    .HasColumnName("Package_Name")
                    .HasMaxLength(50)
                    .IsUnicode(false);
            });

            modelBuilder.Entity<Portfolio>(entity =>
            {
                entity.Property(e => e.PortfolioId).HasColumnName("Portfolio_ID");

                entity.Property(e => e.Image).IsUnicode(false);

                entity.Property(e => e.ProfileId).HasColumnName("Profile_ID");

                entity.Property(e => e.Video).IsUnicode(false);

                entity.HasOne(d => d.Profile)
                    .WithMany(p => p.Portfolio)
                    .HasForeignKey(d => d.ProfileId)
                    .HasConstraintName("FK_Portfolio_Profiles");
            });

            modelBuilder.Entity<Profiles>(entity =>
            {
                entity.HasKey(e => e.ProfileId)
                    .HasName("PK_Profile");

                entity.Property(e => e.ProfileId).HasColumnName("Profile_ID");

                entity.Property(e => e.CategoryId).HasColumnName("Category_ID");

                entity.Property(e => e.CityId).HasColumnName("City_ID");

                entity.Property(e => e.Email)
                    .IsRequired()
                    .IsUnicode(false);

                entity.Property(e => e.EmailConfirmed).HasColumnName("Email_Confirmed");

                entity.Property(e => e.FirstName)
                    .IsRequired()
                    .HasColumnName("First_Name")
                    .HasMaxLength(50)
                    .IsUnicode(false);

                entity.Property(e => e.LastName)
                    .IsRequired()
                    .HasColumnName("Last_Name")
                    .HasMaxLength(50)
                    .IsUnicode(false);

                entity.Property(e => e.Password)
                    .IsRequired()
                    .IsUnicode(false);

                entity.Property(e => e.PhoneNumber).HasColumnName("Phone_Number");

                entity.Property(e => e.PhoneNumberConfirmed).HasColumnName("PhoneNumber_Confirmed");

                entity.Property(e => e.ProfilePicture)
                    .HasColumnName("Profile_Picture")
                    .IsUnicode(false);

                entity.Property(e => e.UserSince)
                    .HasColumnName("User_Since")
                    .HasColumnType("date");

                entity.Property(e => e.Username)
                    .IsRequired()
                    .HasMaxLength(50)
                    .IsUnicode(false);

                entity.HasOne(d => d.Category)
                    .WithMany(p => p.Profiles)
                    .HasForeignKey(d => d.CategoryId)
                    .HasConstraintName("FK_Profiles_Categories");

                entity.HasOne(d => d.City)
                    .WithMany(p => p.Profiles)
                    .HasForeignKey(d => d.CityId)
                    .HasConstraintName("FK_Profiles_Cities");
            });

            modelBuilder.Entity<Reviews>(entity =>
            {
                entity.HasKey(e => e.ReviewId);

                entity.Property(e => e.ReviewId).HasColumnName("Review_ID");

                entity.Property(e => e.FreelancerId).HasColumnName("Freelancer_ID");

                entity.Property(e => e.Message).IsUnicode(false);

                entity.Property(e => e.ProfileId).HasColumnName("Profile_ID");

                entity.HasOne(d => d.Freelancer)
                    .WithMany(p => p.ReviewsFreelancer)
                    .HasForeignKey(d => d.FreelancerId)
                    .HasConstraintName("FK_Reviews_Freelancer");

                entity.HasOne(d => d.Profile)
                    .WithMany(p => p.ReviewsProfile)
                    .HasForeignKey(d => d.ProfileId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("FK_Reviews_Employer");
            });

            modelBuilder.Entity<Roles>(entity =>
            {
                entity.HasKey(e => e.RoleId);

                entity.Property(e => e.RoleId).HasColumnName("Role_ID");

                entity.Property(e => e.RoleName)
                    .IsRequired()
                    .HasColumnName("Role_Name")
                    .HasMaxLength(10)
                    .IsUnicode(false)
                    .IsFixedLength();
            });

            modelBuilder.Entity<States>(entity =>
            {
                entity.HasKey(e => e.StateId);

                entity.Property(e => e.StateId).HasColumnName("State_ID");

                entity.Property(e => e.StateName)
                    .IsRequired()
                    .HasColumnName("State_Name")
                    .HasMaxLength(50)
                    .IsUnicode(false);
            });

            modelBuilder.Entity<UserRoles>(entity =>
            {
                entity.HasKey(e => new { e.RoleId, e.ProfileId });

                entity.Property(e => e.RoleId).HasColumnName("Role_ID");

                entity.Property(e => e.ProfileId).HasColumnName("Profile_ID");

                entity.HasOne(d => d.Profile)
                    .WithMany(p => p.UserRoles)
                    .HasForeignKey(d => d.ProfileId)
                    .HasConstraintName("FK_UserRoles_Profiles");

                entity.HasOne(d => d.Role)
                    .WithMany(p => p.UserRoles)
                    .HasForeignKey(d => d.RoleId)
                    .HasConstraintName("FK_UserRoles_Roles");
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
